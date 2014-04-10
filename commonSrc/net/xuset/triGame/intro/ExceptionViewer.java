package net.xuset.triGame.intro;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.system.IDrawBoard;
import net.xuset.tSquare.system.input.mouse.IMouseListener;
import net.xuset.tSquare.ui.Alignment;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiController;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiLabel;
import net.xuset.tSquare.ui.layout.UiBorderLayout;
import net.xuset.tSquare.ui.layout.UiLayout;
import net.xuset.triGame.Params;

public class ExceptionViewer {
	
	private final IDrawBoard drawBoard;
	private final UiController ui;
	private final ErrorReport errorReport;
	
	public ExceptionViewer(IDrawBoard drawBoard, IMouseListener mouse, Exception ex) {
		this.drawBoard = drawBoard;
		errorReport = new ErrorReport(ex);
		
		new Thread(new Runnable() {
			@Override
			public void run() { errorReport.postReport(Params.ERROR_REPORT_URL); }
		}, "Report sender").start();
		
		ui = new UiController(mouse);
		setupForm();
		loop();
	}
	
	private void setupForm() {
		ui.getForm().setOpaque(true);
		ui.getForm().setBackground(new TsColor(235, 235, 235));
		ui.getForm().setLayout(new UiBorderLayout(ui.getForm()));
		
		UiForm frmMain = new UiForm();
		UiLayout layout = frmMain.getLayout();
		layout.setOrientation(Axis.Y_AXIS);
		layout.setAlignment(Axis.X_AXIS, Alignment.CENTER);
		
		UiLabel lblGretting = new UiLabel("An error was encountered and the game has ended.");
		
		
		layout.add(lblGretting);
		layout.add(new UiLabel("Sorry about this, I hate these bugs just as much as you "));
		layout.add(new UiLabel("do. A bug report was sent and the problem will be "));
		layout.add(new UiLabel("fixed in the next update."));
		
		ui.getForm().getLayout().add(frmMain);
	}
	
	private void loop() {
		while (true) {
			IGraphics g = drawBoard.getGraphics();
			if (g != null) {
				g.clear();
				ui.draw(g);
				drawBoard.flushScreen();
			}
			
			
			try { Thread.sleep(50); } catch (InterruptedException ex) { }
		}
	}
}
