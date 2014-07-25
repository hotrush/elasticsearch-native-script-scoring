package org.elasticsearch.scoring.nativescript.script;

import java.util.*;

import org.elasticsearch.script.ScriptException;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.elasticsearch.index.fielddata.ScriptDocValues;
//import org.elasticsearch.script.AbstractFloatSearchScript;
import org.elasticsearch.script.AbstractSearchScript;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;

//public class IntersectMultipleScriptFactory implements NativeScriptFactory {
//
//    @Override
//    public ExecutableScript newScript(@Nullable Map<String, Object> params) {
//        int limit = params == null ? null : XContentMapValues.nodeIntegerValue(params.get("limit"), 0);
//        int multiple = params == null ? null : XContentMapValues.nodeIntegerValue(params.get("multiple"), 0);
//        String field = params == null ? null : XContentMapValues.nodeStringValue(params.get("field"), null);
//        Map<String, Object> items = params == null ? null : XContentMapValues.nodeMapValue(params.get("items"), null);
//        if (field == null || items == null) {
//            throw new ScriptException("Missing the field parameter");
//        }
//        return new IntersectMultipleScript(field,items,limit,multiple);
//    }
//
//    private static class IntersectMultipleScript extends AbstractFloatSearchScript {
//
//        private final String field;
//        private final int limit;
//        private final int multiple;
//        private final ArrayList<String> items;
//
//        public IntersectMultipleScript(String field,Map<String, Object> items,int limit, int multiple) {
//            this.field = field;
//            this.limit = limit;
//            this.multiple = multiple;
//            ArrayList<String> list = new ArrayList<String>();
//            for (Object value : items.values()) {
//                //this.items.add(value.getValue());
//                list.add(value.toString());
//            }
//            this.items = list;
//        }
//
//        @Override
//        public float runAsFloat() {
//            ScriptDocValues source_doc_values = (ScriptDocValues) doc().get(field);
//            if (source_doc_values != null && !source_doc_values.isEmpty()) {
//
//                List<String> source_items_list = ((ScriptDocValues.Strings) source_doc_values).getValues();
//                ArrayList<String> source_items = new ArrayList<String>(source_items_list.size());
//                source_items.addAll(source_items_list);
//
//                source_items.retainAll(items);
//                int intersections_num = source_items.size();
//                if (intersections_num > 0) {
//                    if (intersections_num >= limit) {
//                        return (float) score() + limit*multiple;
//                    } else {
//                        return (float) score() + intersections_num*multiple;
//                    }
//                }
//            }
//            return score();
//        }
//    }
//}

public class IntersectMultipleScript extends AbstractSearchScript {

    int limit;
    int multiple;
    String field;
    ArrayList<String> items;
    ArrayList<String> source_items;

    final static public String SCRIPT_NAME = "multiple_terms_script_score";

    public static class Factory implements NativeScriptFactory {
        @Override
        public ExecutableScript newScript(@Nullable Map<String, Object> params) {
            return new IntersectMultipleScript(params);
        }
    }

    private IntersectMultipleScript(Map<String, Object> params) {
        params.entrySet();
        // get the items
        items = (ArrayList<String>) params.get("items");
        // field name
        field = (String) params.get("field");
        // get doc items
//        if (field != null) {
//            source_items = (ArrayList<String>) doc().get(field);
//        }
        // get the field
        limit = (int) params.get("limit");
        multiple = (int) params.get("multiple");
        if (field == null || items == null) {
            throw new ScriptException("cannot initialize " + SCRIPT_NAME + ": field or items parameter missing!");
        }
    }

    @Override
    public Object run() {
        //source_items = (ArrayList<String>) doc().get(field);
        ScriptDocValues source_doc_values = (ScriptDocValues) doc().get(field);
        if (source_doc_values != null && !source_doc_values.isEmpty()) {

            List<String> source_items_list = ((ScriptDocValues.Strings) source_doc_values).getValues();
            ArrayList<String> source_items = new ArrayList<String>(source_items_list.size());
            source_items.addAll(source_items_list);

            source_items.retainAll(items);
            int intersections_num = source_items.size();
            if (intersections_num > 0) {
                if (intersections_num >= limit) {
                    //return (float) score() + limit*multiple;
                    return (float) limit*multiple;
                } else {
                    //return (float) score() + intersections_num*multiple;
                    return (float) intersections_num*multiple;
                }
            }
        }
        //return score();
        return 0;
    }

}
