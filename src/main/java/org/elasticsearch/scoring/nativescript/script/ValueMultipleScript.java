package org.elasticsearch.scoring.nativescript.script;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import org.elasticsearch.script.ScriptException;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.elasticsearch.index.fielddata.ScriptDocValues;
import org.elasticsearch.index.fielddata.ScriptDocValues.Longs;
import org.elasticsearch.script.AbstractSearchScript;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;

public class ValueMultipleScript extends AbstractSearchScript {

    String field;
    ArrayList<HashMap<String, Long>> value_multiples;
    int fieldValue;

    final static public String SCRIPT_NAME = "value_multiple_script_score";

    public static class Factory implements NativeScriptFactory {
        @Override
        public ExecutableScript newScript(@Nullable Map<String, Object> params) {
            return new ValueMultipleScript(params);
        }
    }

    private ValueMultipleScript(Map<String, Object> params) {
        params.entrySet();
        // get the items
        value_multiples = (ArrayList<HashMap<String, Long>>) params.get("value_multiples");
        // field name
        field = (String) params.get("field");
        if (field == null || value_multiples == null) {
            throw new ScriptException("cannot initialize " + SCRIPT_NAME + ": field or value_multiples parameter missing!");
        }
    }

    @Override
    public Object run() {

        ScriptDocValues source_doc_value = (ScriptDocValues) doc().get(field);
        if (source_doc_value != null && !source_doc_value.isEmpty()) {

            //for(Iterator<String> i = someList.iterator(); i.hasNext(); ) {
            //    String item = i.next();
            //    System.out.println(item);
            //}

            //Float fieldValue = ((ScriptDocValues) source_doc_value).getValue();
            //ScriptDocValues.Doubles fieldValue = (ScriptDocValues.Doubles) source_doc_value;
            Long fieldValue = ((ScriptDocValues.Longs) source_doc_value).getValue();
            if (fieldValue != null){
                return fieldValue;
            }
            for (HashMap<String, Long> value_multiple : value_multiples) {
                if ((Long) value_multiple.get("value") == fieldValue) {
//                //if (value_multiple.get("value") == 2) {
//                    //return score()*value_multiple.get("multiple");
                    return score()*1000;
                }
            }
        }
        return score();
    }

}
