package ws.l10n.android.gradle;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.compile.AbstractCompile;

import java.util.Set;

public class L10nPlugin implements Plugin<Project> {

    public void apply(Project target) {
        target.getLogger().info("L10nPlugin apply");

        target.getExtensions().create("l10nOptions", L10nExtension.class);
        target.getTasks().create("loadMessages", LoadMessagesTask.class);

        final Set<Task> loadMessages = target.getTasksByName("loadMessages", true);
        target.getTasks().withType(AbstractCompile.class, new Action<Task>() {
            public void execute(Task compileTask) {
                compileTask.dependsOn(loadMessages);
            }
        });

    }

}