package org.elasticsearch.scoring.nativescript.plugin;

import org.elasticsearch.scoring.nativescript.script.IntersectMultipleStringScriptFactory;
import org.elasticsearch.scoring.nativescript.script.IntersectMultipleIntegerScriptFactory;
import org.elasticsearch.scoring.nativescript.script.ValueMultipleScriptFactory;
import org.elasticsearch.scoring.nativescript.script.DoubleValueMultipleScriptFactory;
import org.elasticsearch.scoring.nativescript.script.ValueIncrementScriptFactory;
import org.elasticsearch.scoring.nativescript.script.ValueInRangeScriptFactory;
import org.elasticsearch.plugins.AbstractPlugin;
import org.elasticsearch.script.ScriptModule;

/**
 * This class is instantiated when Elasticsearch loads the plugin for the
 * first time. If you change the name of this plugin, make sure to update
 * src/main/resources/es-plugin.properties file that points to this class.
 */
public class NativeScriptScoringPlugin extends AbstractPlugin {

    /**
     * The name of the plugin.
     * <p/>
     * This name will be used by elasticsearch in the log file to refer to this plugin.
     *
     * @return plugin name.
     */
    @Override
    public String name() {
        return "native-script-scoring";
    }

    /**
     * The description of the plugin.
     *
     * @return plugin description
     */
    @Override
    public String description() {
        return "Native script advanced score";
    }

    public void onModule(ScriptModule module) {
        // Register each script that we defined in this plugin
//        module.registerScript(IntersectMultipleScript.SCRIPT_NAME, IntersectMultipleScript.Factory.class);
        module.registerScript("multiple_terms_string_script_score", IntersectMultipleStringScriptFactory.class);
        module.registerScript("multiple_terms_integer_script_score", IntersectMultipleIntegerScriptFactory.class);
//        module.registerScript(ValueMultipleScript.SCRIPT_NAME, ValueMultipleScript.Factory.class);
        module.registerScript("value_multiple_script_score", ValueMultipleScriptFactory.class);
        module.registerScript("double_value_multiple_script_score", DoubleValueMultipleScriptFactory.class);
        module.registerScript("value_increment_script_score", ValueIncrementScriptFactory.class);
        module.registerScript("value_in_range_script_score", ValueInRangeScriptFactory.class);
    }
}
