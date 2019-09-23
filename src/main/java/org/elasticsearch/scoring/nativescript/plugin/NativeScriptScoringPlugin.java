package org.elasticsearch.scoring.nativescript.plugin;

import org.elasticsearch.script.NativeScriptFactory;
import org.elasticsearch.scoring.nativescript.script.IntersectMultipleStringScriptFactory;
import org.elasticsearch.scoring.nativescript.script.IntersectMultipleIntegerScriptFactory;
import org.elasticsearch.scoring.nativescript.script.ValueMultipleScriptFactory;
import org.elasticsearch.scoring.nativescript.script.DoubleValueMultipleScriptFactory;
import org.elasticsearch.scoring.nativescript.script.ValueIncrementScriptFactory;
import org.elasticsearch.scoring.nativescript.script.ValueInRangeScriptFactory;
import org.elasticsearch.scoring.nativescript.script.RangeHasValueScriptFactory;
import org.elasticsearch.plugins.ScriptPlugin;
import org.elasticsearch.script.ScriptModule;

import java.util.*;

public class NativeScriptScoringPlugin implements ScriptPlugin {

    public List<NativeScriptFactory> getNativeScripts() {
        List<NativeScriptFactory> scriptsList = new ArrayList<NativeScriptFactory>();
        scriptsList.add(new IntersectMultipleStringScriptFactory());
        scriptsList.add(new IntersectMultipleIntegerScriptFactory());
        scriptsList.add(new ValueMultipleScriptFactory());
        scriptsList.add(new DoubleValueMultipleScriptFactory());
        scriptsList.add(new ValueIncrementScriptFactory());
        scriptsList.add(new ValueInRangeScriptFactory());
        scriptsList.add(new RangeHasValueScriptFactory());
        return scriptsList;
    }
}
