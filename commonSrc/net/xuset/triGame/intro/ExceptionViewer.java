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
import net.xuset.tSquare.ui.UiSpacer;
import net.xuset.tSquare.ui.layout.UiBorderLayout;
import net.xuset.tSquare.ui.layout.UiLayout;
import net.xuset.tSquare.ui.layout.UiQueueLayout;

public class ExceptionViewer {
	
	private final IDrawBoard drawBoard;
	private final UiController ui;
	
	public ExceptionViewer(IDrawBoard drawBoard, IMouseListener mouse, Exception ex) {
		this.drawBoard = drawBoard;
		ui = new UiController(mouse);
		setupForm(ex);
		loop();
	}
	
	private void setupForm(Exception ex) {
		ui.getForm().setOpaque(true);
		ui.getForm().setBackground(new TsColor(235, 235, 235));
		ui.getForm().setLayout(new UiBorderLayout(ui.getForm()));
		
		UiForm frmMain = new UiForm();
		UiLayout layout = frmMain.getLayout();
		layout.setOrientation(Axis.Y_AXIS);
		layout.setAlignment(Axis.X_AXIS, Alignment.CENTER);
		
		UiLabel lblGretting = new UiLabel("An error was encountered and the game has ended.");
		UiLabel lblApology = new UiLabel("Sorry about this. Below is a description of the error.");
		
		layout.add(lblGretting);
		layout.add(lblApology);
		layout.add(new UiSpacer(1, 50));
		layout.add(createStackTraceForm(ex));
		
		ui.getForm().getLayout().add(frmMain);
	}
	
	private UiForm createStackTraceForm(Exception ex) {
		UiForm frmStack = new UiForm();
		UiLayout layout = new UiQueueLayout(frmStack);
		frmStack.setLayout(layout);
		layout.setOrientation(Axis.Y_AXIS);
		frmStack.getBorder().setVisibility(true);
		
		layout.add(new UiLabel(ex.toString()));
		
		for (StackTraceElement element : ex.getStackTrace()) {
			String text = "      at " + element.toString();
			layout.add(new UiLabel(text));
		}
		
		return frmStack;
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
