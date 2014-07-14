package org.elasticsearch.scoring.nativescript.script;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.elasticsearch.script.ScriptException;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.elasticsearch.index.fielddata.ScriptDocValues;
import org.elasticsearch.index.fielddata.ScriptDocValues.Longs;
import org.elasticsearch.script.AbstractSearchScript;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;

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
        source_items = (ArrayList<String>) doc().get(field);
        source_items.retainAll(items);
        int intersections_num = source_items.size();
        if (intersections_num > 0) {
            if (intersections_num >= limit) {
                return score() + limit*multiple;
            } else {
                return score() + intersections_num*multiple;
            }
        }
        return score();
    }

}
