/*
 * Copyright 2015-present Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.maojianwei.maoqos.cli;

import org.apache.karaf.shell.commands.Command;
import org.onosproject.cli.AbstractShellCommand;
import com.maojianwei.maoqos.base.DeviceElement;
import com.maojianwei.maoqos.intf.MaoPipelineService;
import org.onosproject.net.DeviceId;

import java.util.Map;


@Command(scope = "onos", name = "qos-debug",
        description = "Mao Qos Debug")
public class Debug extends AbstractShellCommand {


    @Override
    protected void execute() {

        MaoPipelineService maoPipelineManager = get(MaoPipelineService.class);

        Map<DeviceId, DeviceElement> map = maoPipelineManager.debug();

        int countSTANDBY = 0;
        int countINIT = 0;
        int countOTHER = 0;
        for(Map.Entry<DeviceId, DeviceElement> de : map.entrySet()){
            if(de.getValue().getState() == DeviceElement.State.STANDBY){
                countSTANDBY++;
            }else if(de.getValue().getState() == DeviceElement.State.INIT_WAIT_PORT){
                countINIT++;
            }else{
                countOTHER++;
            }
            print("%s\t%-16.16s\t%s",de.getKey().uri().getSchemeSpecificPart(), de.getValue().getState(), de.getValue().getPortMap().toString());
        }

        print("\n\nCount: %d, Standby: %d, InitWaitPort: %d, Other: %d\nPoolSize: %d, ActiveCount: %d, TaskCount: %d, CompleteTask: %d",
              map.size(), countSTANDBY, countINIT, countOTHER,
              DeviceElement.getPoolSize(), DeviceElement.getActiveCount(), DeviceElement.getTaskCount(), DeviceElement.getCompletedTaskCount());
    }
}
