package melonslise.locks.client.init;

import melonslise.locks.common.network.toclient.*;

public final class LocksNetworkClient
{
	private LocksNetworkClient() {}

	public static void register()
	{
		//S2C
		ClientNet.register();
	}
}