/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phoenixendpointclient.phoenix.endpoint.client.events;

import com.google.common.eventbus.EventBus;
import lombok.Getter;

/**
 *
 * @author jdcs_dev
 */
public class ClientEventHEADER {

    @Getter
    private static final EventBus FORSEARCHANSWER_EVENT_BUS = new EventBus();

    @Getter
    private static final EventBus FORMESSAG_EVENT_BUS = new EventBus();

    @Getter
    private static final EventBus REQUEST_REMOTE_EVENT_BUS = new EventBus();

    @Getter
    private static final EventBus REQUEST_LOCAL_EVENT_BUS = new EventBus();

}
