package org.elasticsearch.scoring.nativescript.script;

import java.util.*;

import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.elasticsearch.index.fielddata.ScriptDocValues;
import org.elasticsearch.index.fielddata.ScriptDocValues.Longs;
import org.elasticsearch.script.AbstractDoubleSearchScript;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;

public class RangeHasValueScriptFactory implements NativeScriptFactory {

    @Override
    public ExecutableScript newScript(@Nullable Map<String, Object> params) {
        int multiple = params == null ? null : XContentMapValues.nodeIntegerValue(params.get("multiple"), 0);
        long range_value1 = params == null ? null : XContentMapValues.nodeLongValue(params.get("range_value1"), 0);
        long range_value2 = params == null ? null : XContentMapValues.nodeLongValue(params.get("range_value2"), 0);
        String value_field = params == null ? null : XContentMapValues.nodeStringValue(params.get("value_field"), null);
        float value_miltiplier = params == null ? null : XContentMapValues.nodeFloatValue(params.get("value_miltiplier"), 1);
        return new RangeHasValueScript(multiple,range_value1,range_value2,value_field,value_miltiplier);
    }

    @Override
    public boolean needsScores() {
        return false;
    }

    @Override
    public String getName() {
        return "range_has_value_script_score";
    }

    private static class RangeHasValueScript extends AbstractDoubleSearchScript {

        private final int multiple;
        private final long range_value1;
        private final long range_value2;
        private final String value_field;
        private final float value_miltiplier;

        public RangeHasValueScript(int multiple,long range_value1,long range_value2,String value_field, float value_miltiplier) {
            this.multiple = multiple;
            this.range_value1 = range_value1;
            this.range_value2 = range_value2;
            this.value_field = value_field;
            this.value_miltiplier = value_miltiplier;
        }

        @Override
        public double runAsDouble() {
            ScriptDocValues sourceValue = (ScriptDocValues) doc().get(value_field);

            if (sourceValue != null && !sourceValue.isEmpty()) {

                ScriptDocValues.Longs fieldValue = (ScriptDocValues.Longs) sourceValue;
                double multipledValue = fieldValue.getValue() * value_miltiplier;

                if (multipledValue >= range_value1 && multipledValue <= range_value2) {
                    return (double) multiple*1;
                }

            }

            return 0;

        }
    }
}
