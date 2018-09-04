/*
 * Copyright 2016-2018 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.galleon.cli.cmd.state;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.aesh.command.CommandDefinition;
import org.aesh.command.impl.completer.FileOptionCompleter;
import org.aesh.command.option.Argument;
import org.aesh.io.Resource;
import org.jboss.galleon.cli.CommandExecutionException;
import org.jboss.galleon.cli.HelpDescriptions;
import org.jboss.galleon.cli.PmCommandInvocation;
import org.jboss.galleon.cli.PmSessionCommand;
import org.jboss.galleon.cli.cmd.CliErrors;
import org.jboss.galleon.cli.cmd.CommandDomain;
import org.jboss.galleon.cli.model.state.State;
import org.jboss.galleon.config.ProvisioningConfig;
import org.jboss.galleon.xml.ProvisioningXmlWriter;

/**
 *
 * @author jdenise@redhat.com
 */
@CommandDefinition(name = "export", description = HelpDescriptions.EXPORT_STATE)
public class StateExportCommand extends PmSessionCommand {

    @Argument(completer = FileOptionCompleter.class, required = false,
            description = HelpDescriptions.EXPORT_FILE)
    private Resource file;

    @Override
    protected void runCommand(PmCommandInvocation invoc) throws CommandExecutionException {
        if (file != null) {
            final Resource specResource = file.resolve(invoc.getAeshContext().getCurrentWorkingDirectory()).get(0);
            final Path targetFile = Paths.get(specResource.getAbsolutePath());
            State session = invoc.getPmSession().getState();
            try {
                session.export(targetFile);
            } catch (Exception ex) {
                throw new CommandExecutionException(invoc.getPmSession(), CliErrors.exportProvisionedFailed(), ex);
            }
            invoc.println("Provisioning file generated in " + targetFile);
        } else {
            ByteArrayOutputStream output = null;
            try {
                ProvisioningConfig config = invoc.getPmSession().getState().getConfig();
                output = new ByteArrayOutputStream();
                PrintWriter writer = new PrintWriter(output);
                ProvisioningXmlWriter.getInstance().write(config, writer);
            } catch (Exception e) {
                throw new CommandExecutionException(invoc.getPmSession(), CliErrors.exportProvisionedFailed(), e);
            }
            invoc.println(output.toString());
        }
    }

    @Override
    public CommandDomain getDomain() {
        return CommandDomain.EDITING;
    }

}
