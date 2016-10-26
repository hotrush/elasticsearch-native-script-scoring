package org.elasticsearch.plugin.scoring;

import org.elasticsearch.plugin.scoring.IntersectMultipleStringScriptFactory;
import org.elasticsearch.plugin.scoring.IntersectMultipleIntegerScriptFactory;
import org.elasticsearch.plugin.scoring.ValueMultipleScriptFactory;
import org.elasticsearch.plugin.scoring.DoubleValueMultipleScriptFactory;
import org.elasticsearch.plugin.scoring.ValueIncrementScriptFactory;
import org.elasticsearch.plugin.scoring.ValueInRangeScriptFactory;
import org.elasticsearch.plugin.scoring.RangeHasValueScriptFactory;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.script.ScriptModule;

/**
 * This class is instantiated when Elasticsearch loads the plugin for the
 * first time. If you change the name of this plugin, make sure to update
 * src/main/resources/es-plugin.properties file that points to this class.
 */
public class NativeScriptScoringPlugin extends Plugin {

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
        module.registerScript("range_has_value_script_score", RangeHasValueScriptFactory.class);
    }
}
