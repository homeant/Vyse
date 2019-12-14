package fun.vyse.cloud.provider;

/**
 * PropertyMachineIdProvider
 *
 * @author junchen
 * @date 2019-12-14 15:02
 */
public class PropertyMachineIdProvider implements MachineIdProvider {
	private long machineId;

	@Override
	public long getMachineId() {
		return machineId;
	}

	public void setMachineId(long machineId) {
		this.machineId = machineId;
	}
}
