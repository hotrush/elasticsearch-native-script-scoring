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

public class ValueInRangeScriptFactory implements NativeScriptFactory {

    @Override
    public ExecutableScript newScript(@Nullable Map<String, Object> params) {
        int multiple = params == null ? null : XContentMapValues.nodeIntegerValue(params.get("multiple"), 0);
        String range_field1 = params == null ? null : XContentMapValues.nodeStringValue(params.get("range_field1"), null);
        String range_field2 = params == null ? null : XContentMapValues.nodeStringValue(params.get("range_field2"), null);
        long value = params == null ? null : XContentMapValues.nodeLongValue(params.get("value"), 0);
        return new ValueInRangeScript(multiple,range_field1,range_field2,value);
    }

    private static class ValueInRangeScript extends AbstractFloatSearchScript {

        private final int multiple;
        private final String range_field1;
        private final String range_field2;
        private final long value;

        public ValueInRangeScript(int multiple,String range_field1,String range_field2,long value) {
            this.multiple = multiple;
            this.range_field1 = range_field1;
            this.range_field2 = range_field2;
            this.value = value;
        }

        @Override
        public float runAsFloat() {
            ScriptDocValues sourceRangeDoc1 = (ScriptDocValues) doc().get(range_field1);
            ScriptDocValues sourceRangeDoc2 = (ScriptDocValues) doc().get(range_field2);

            if (sourceRangeDoc1 != null && !sourceRangeDoc1.isEmpty() && sourceRangeDoc2 != null && !sourceRangeDoc2.isEmpty()) {

                ScriptDocValues.Longs fieldRangeValue1 = (ScriptDocValues.Longs) sourceRangeDoc1;
                ScriptDocValues.Longs fieldRangeValue2 = (ScriptDocValues.Longs) sourceRangeDoc2;

                if (value >= fieldRangeValue1.getValue() && value <= fieldRangeValue2.getValue()) {
                    return (float) multiple*1;
                }

            }

            return 0;

        }
    }
}
