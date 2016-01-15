package ws.l10n.gradle;

import org.gradle.api.Project;
import org.gradle.api.Plugin;

public class L10nPlugin implements Plugin<Project> {

    @Override
    public void apply(Project target) {

        target.getExtensions().create("l10nOptions", L10nExtension.class);
        target.task("loadMessages");
        target.getLogger().warn("L10nPlugin");
    }

}