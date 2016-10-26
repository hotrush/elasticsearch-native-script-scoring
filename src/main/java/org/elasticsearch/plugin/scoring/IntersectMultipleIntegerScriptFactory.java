package org.elasticsearch.plugin.scoring;

import java.util.*;

import org.elasticsearch.script.ScriptException;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.elasticsearch.index.fielddata.ScriptDocValues;
//import org.elasticsearch.script.AbstractFloatSearchScript;
import org.elasticsearch.script.AbstractFloatSearchScript;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;

public class IntersectMultipleIntegerScriptFactory implements NativeScriptFactory {

    @Override
    public ExecutableScript newScript(@Nullable Map<String, Object> params) {
        int limit = params == null ? null : XContentMapValues.nodeIntegerValue(params.get("limit"), 0);
        int multiple = params == null ? null : XContentMapValues.nodeIntegerValue(params.get("multiple"), 0);
        String field = params == null ? null : XContentMapValues.nodeStringValue(params.get("field"), null);
        String itemsStr = params == null ? null : XContentMapValues.nodeStringValue(params.get("items"), null);
        ArrayList<Integer> items = new ArrayList<Integer>();
        String[] ss = itemsStr.split(",,,");
        for(int i=0;i<ss.length;i++) {
           items.add(Integer.parseInt(ss[i]));
        }
        if (field == null || items == null) {
            throw new ScriptException("Missing the field parameter");
        }
        return new IntersectMultipleIntegerScript(field,items,limit,multiple);
    }

    @Override
    public boolean needsScores() {
      return false;
    }

    private static class IntersectMultipleIntegerScript extends AbstractFloatSearchScript {

        private final String field;
        private final int limit;
        private final int multiple;
        private final ArrayList<Integer> items;

        public IntersectMultipleIntegerScript(String field,ArrayList<Integer> items,int limit, int multiple) {
            this.field = field;
            this.limit = limit;
            this.multiple = multiple;
            this.items = items;
        }

        @Override
        public float runAsFloat() {
            ScriptDocValues source_doc_values = (ScriptDocValues) doc().get(field);
            if (source_doc_values != null && !source_doc_values.isEmpty()) {

                List<Long> source_items_list = ((ScriptDocValues.Longs) source_doc_values).getValues();
                ArrayList<Integer> source_items = new ArrayList<Integer>(source_items_list.size());
                for(int i=0;i<source_items_list.size();i++) {
                    source_items.add(source_items_list.get(i).intValue());
                }

                source_items.retainAll(items);
                int intersections_num = source_items.size();
                if (intersections_num > 0) {
                    if (intersections_num >= limit) {
                        return (float) limit*multiple;
                    } else {
                        return (float) intersections_num*multiple;
                    }
                }
            }
            return 0;
        }
    }
}
