package org.elasticsearch.scoring.nativescript.script;

import java.util.*;

import org.elasticsearch.script.ScriptException;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.elasticsearch.index.fielddata.ScriptDocValues;
import org.elasticsearch.index.fielddata.ScriptDocValues.Longs;
import org.elasticsearch.script.AbstractFloatSearchScript;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;

public class DoubleValueMultipleScriptFactory implements NativeScriptFactory {

    @Override
    public ExecutableScript newScript(@Nullable Map<String, Object> params) {
        int multiple = params == null ? null : XContentMapValues.nodeIntegerValue(params.get("multiple"), 0);
        String itemsStr1 = params == null ? null : XContentMapValues.nodeStringValue(params.get("values1"), null);
        String itemsStr2 = params == null ? null : XContentMapValues.nodeStringValue(params.get("values2"), null);
        ArrayList<String> values1 = new ArrayList<String>(Arrays.asList(itemsStr1.split(",,,")));
        ArrayList<String> values2 = new ArrayList<String>(Arrays.asList(itemsStr2.split(",,,")));
        String field1 = params == null ? null : XContentMapValues.nodeStringValue(params.get("field1"), null);
        String field2 = params == null ? null : XContentMapValues.nodeStringValue(params.get("field2"), null);
        return new DoubleValueMultipleScript(multiple,field1,field2,values1,values2);
    }

    private static class DoubleValueMultipleScript extends AbstractFloatSearchScript {

        private final int multiple;
        private final String field1;
        private final String field2;
        private final ArrayList<String> values1;
        private final ArrayList<String> values2;

        public DoubleValueMultipleScript(int multiple,String field1,String field2,ArrayList<String> values1,ArrayList<String> values2) {
            this.multiple = multiple;
            this.field1 = field1;
            this.field2 = field2;
            this.values1 = values1;
            this.values2 = values2;
        }

        @Override
        public float runAsFloat() {
            ScriptDocValues sourceValues1 = (ScriptDocValues) doc().get(field1);
            ScriptDocValues sourceValues2 = (ScriptDocValues) doc().get(field2);

            if (sourceValues1 != null && !sourceValues1.isEmpty() && sourceValues2 != null && !sourceValues2.isEmpty()) {

                List<String> sourceValuesList1 = ((ScriptDocValues.Strings) sourceValues1).getValues();
                ArrayList<String> sourceItems1 = new ArrayList<String>(sourceValuesList1.size());
                sourceItems1.addAll(sourceValuesList1);
                sourceItems1.retainAll(values1);
                int intersections_num1 = sourceItems1.size();

                List<String> sourceValuesList2 = ((ScriptDocValues.Strings) sourceValues2).getValues();
                ArrayList<String> sourceItems2 = new ArrayList<String>(sourceValuesList2.size());
                sourceItems2.addAll(sourceValuesList2);
                sourceItems2.retainAll(values2);
                int intersections_num2 = sourceItems2.size();

                if (intersections_num1 > 0 && intersections_num2 > 0) {
                    return (float) multiple*1;
                }

            }

            return 0;

        }
    }
}
