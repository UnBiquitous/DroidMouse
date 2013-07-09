package br.unb.unbiquitous.mouse;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.util.Log;
import br.unb.unbiquitous.ubiquitos.network.model.NetworkDevice;
import br.unb.unbiquitous.ubiquitos.uos.adaptabitilyEngine.Gateway;
import br.unb.unbiquitous.ubiquitos.uos.adaptabitilyEngine.NotifyException;
import br.unb.unbiquitous.ubiquitos.uos.adaptabitilyEngine.SmartSpaceGateway;
import br.unb.unbiquitous.ubiquitos.uos.application.UOSMessageContext;
import br.unb.unbiquitous.ubiquitos.uos.driverManager.drivers.Clickable;
import br.unb.unbiquitous.ubiquitos.uos.driverManager.drivers.DefaultDrivers;
import br.unb.unbiquitous.ubiquitos.uos.driverManager.drivers.Mouse;
import br.unb.unbiquitous.ubiquitos.uos.driverManager.drivers.Pointer;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.dataType.UpDevice;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.dataType.UpDriver;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.dataType.UpNetworkInterface;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.dataType.UpService;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.dataType.UpService.ParameterType;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.Notify;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.ServiceCall;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.ServiceResponse;

public class MouseDriver implements Clickable {

	private String instanceId;
	private SmartSpaceGateway gateway;
    private List<UpNetworkInterface> listenerDevices;
    private List<UpDriver> parent;
	
    public MouseDriver() {
    	this.parent = new ArrayList<UpDriver>();
	}

	@Override
	public void init(Gateway gateway, String instanceId) {
		this.gateway = (SmartSpaceGateway)gateway;
		this.instanceId = instanceId;
		this.listenerDevices = new ArrayList<UpNetworkInterface>();
		Log.d("DROID MOUSE", "mouseDriver.init()");
		Log.d("DROID MOUSE", "instanceId: " + instanceId);
	}
	
	/**
     * Registers the message sender as a Mouse Events Listener
     *
     * @param name serviceCall Message coming from the application.
     * @param name serviceResponse Message to be sent back to the application
     * @param name networkDevice Information about the network used for communication.
     * @since 2011.0717
     */
    public void registerListener(ServiceCall serviceCall,
            ServiceResponse serviceResponse, UOSMessageContext messageContext) {
       
    	Log.d("DROID MOUSE", "registerListener");
        NetworkDevice networkDevice = messageContext.getCallerDevice();
       
        StringTokenizer tok = new StringTokenizer(networkDevice.getNetworkDeviceName(),":"); 
        String host = tok.nextToken();
        Log.d("DROID MOUSE", "host: " + host);
       
        UpNetworkInterface uni = new UpNetworkInterface(
                networkDevice.getNetworkDeviceType(), host);//FIXME: [B&M] DeviceName ou só host? o cache funciona mas nenhuma mensagem é enviada.
        if (!listenerDevices.contains(uni)){
        	Log.d("DROID MOUSE", "adicionou listener" );
            listenerDevices.add(uni);
        }
        
    }

    /**
     * Unregisters the message sender from the list of Mouse Events Listener.
     *
     * @param name serviceCall Message coming from the application.
     * @param name serviceResponse Message to be sent back to the application
     * @param name networkDevice Information about the network used for communication.
     * @since 2011.0717
     */
    public void unregisterListener(ServiceCall serviceCall,
            ServiceResponse serviceResponse, UOSMessageContext messageContext) {
       
        NetworkDevice networkDevice = messageContext.getCallerDevice();
       
        StringTokenizer tok = new StringTokenizer(networkDevice.getNetworkDeviceName(),":");
        String host = tok.nextToken();
       
        UpNetworkInterface uni = new UpNetworkInterface(
                networkDevice.getNetworkDeviceType(), host);
        listenerDevices.remove(uni);
    }
    
	@Override
	public void move(int axisX, int axisY) {
		Notify notify = new Notify(MOVE_EVENT)
			.addParameter(AXIS_X, String.valueOf(axisX))
			.addParameter(AXIS_Y, String.valueOf(axisY));

        Log.d("DROID MOUSE", "moveu" );
        sendEvent(notify);
	}

	@Override
	public void buttonPressed(int button) {
		raiseButtonEvent(button, BUTTON_PRESSED_EVENT);
	}

	@Override
	public void buttonReleased(int button) {
		raiseButtonEvent(button, BUTTON_RELEASED_EVENT);
	}

	private void raiseButtonEvent(Integer button, String event) {
		Notify notify = new Notify(event)
			.addParameter(Clickable.BUTTON, button.toString());

        sendEvent(notify);
	}

	private void sendEvent(Notify notify) {
		
		notify.setDriver(Clickable.DRIVER_NAME);
        notify.setInstanceId(instanceId);
		
		for (int i = 0 ; i < listenerDevices.size(); i++){
	        UpNetworkInterface uni = (UpNetworkInterface) listenerDevices.get(i);
	        UpDevice device = new UpDevice("Anonymous");
	        device.addNetworkInterface(uni.getNetworkAddress(), uni.getNetType());
	        try {
	            this.gateway.sendEventNotify(notify, device);
	        } catch (NotifyException e) {
	        	Log.e("MOUSE DRIVER", e.getMessage());
	        }
	    }
	}

	@Override
	public UpDriver getDriver() {
		UpDriver clickableDriver = new UpDriver(Clickable.DRIVER_NAME);
		
		UpService register = new UpService("registerListener").addParameter("eventKey", ParameterType.MANDATORY);
		UpService unregister = new UpService("unregisterListener").addParameter("eventKey", ParameterType.OPTIONAL);
		
		UpService move = new UpService(Pointer.MOVE_EVENT).addParameter(Pointer.AXIS_X, ParameterType.MANDATORY)
				.addParameter(Pointer.AXIS_Y, ParameterType.MANDATORY);
		UpService buttonPressed = new UpService(Clickable.BUTTON_PRESSED_EVENT).addParameter(Clickable.BUTTON, ParameterType.MANDATORY);
	    UpService buttonReleased = new UpService(Clickable.BUTTON_RELEASED_EVENT).addParameter(Clickable.BUTTON, ParameterType.MANDATORY);
	    
	    clickableDriver.addService(register);
	    clickableDriver.addService(unregister);
	    
	    clickableDriver.addEvent(move);
	    clickableDriver.addEvent(buttonPressed);
	    clickableDriver.addEvent(buttonReleased);
	    
		parent.add(DefaultDrivers.POINTER.getDriver());
		
		clickableDriver.addEquivalentDrivers(DefaultDrivers.POINTER.getDriver().getName());
		
        return clickableDriver;
	}

	@Override
	public List<UpDriver> getParent() {
		return this.parent;
	}

	@Override
	public void destroy() {
		//Log
		
	}

}
