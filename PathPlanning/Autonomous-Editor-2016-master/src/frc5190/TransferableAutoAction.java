package frc5190;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import frc5190.auto.actions.AutoAction;

public class TransferableAutoAction implements Transferable {

	private AutoAction autoAction;
	
	public TransferableAutoAction(AutoAction autoAction) {
		this.autoAction = autoAction;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] {};
	}

	public AutoAction getAutoAction() {
		return autoAction;
	}
	
	@Override
	public boolean isDataFlavorSupported(DataFlavor paramDataFlavor) {
		return true;
	}

	@Override
	public Object getTransferData(DataFlavor paramDataFlavor) throws UnsupportedFlavorException, IOException {
		return autoAction;
	}

}
