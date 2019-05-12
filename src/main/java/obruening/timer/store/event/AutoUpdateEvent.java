package obruening.timer.store.event;

public class AutoUpdateEvent {

	private Boolean enabled;
    
	public AutoUpdateEvent(Boolean enabled) {
        this.enabled = enabled;
    }
	
    public Boolean isEnabled() {
		return enabled;
	}
}
