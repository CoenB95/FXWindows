package com.cbapps.javafx.idbike;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import com.cbapps.javafx.idbike.model.param.*;
import com.cbapps.javafx.idbike.model.param.ParamManager.Menu;

import javafx.animation.FadeTransition;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class NextGenBicycleScene {

	private ParamManager manager;
	
	public NextGenBicycleScene() {
		manager = new ParamManager();
	}
	
	public GridPane buildControls(Stage stage) {
		GridPane controls = new GridPane();
		controls.setPadding(new Insets(10));
		controls.setEffect(new DropShadow(8, Color.BLACK));
		controls.setBackground(new Background(new BackgroundFill(Color.WHITE,
				new CornerRadii(5), new Insets(5))));
		FancyProgressBar pro = new FancyProgressBar();
		pro.setVisible(false);
		Text title = new Text("IDbike");
		title.setFont(Font.loadFont(NextGenBicycleScene.class
				.getResourceAsStream("resources/"
						+ "Roboto-Medium.ttf"), 30));
		Label chosen_file = new Label("Geen bestand");
		chosen_file.setPadding(new Insets(10));
		chosen_file.setWrapText(true);
		Button button = new Button("Openen");
		button.setOnAction(event -> {
			pro.setVisible(true);
			FadeTransition ft = new FadeTransition();
			ft.setNode(pro);
			ft.setFromValue(0); ft.setToValue(1); ft.play();
			FileChooser fc = new FileChooser();
			fc.setTitle("Kies bestand");
			fc.getExtensionFilters().add(new ExtensionFilter(".ste", "*.ste"));
			File file = fc.showOpenDialog(stage);
			if (file != null) {
				chosen_file.setText(file.getAbsolutePath());
				manager.clearParams();
				try {
					readFile(file);
				} catch (Exception e) {
					chosen_file.setText("Iets ging mis.");
					e.printStackTrace();
				}
			} else {
				chosen_file.setText("Geen bestand gekozen");
			}
			ft.setFromValue(1); ft.setToValue(0); ft.playFromStart();
		});
		Button button_save = new Button("Opslaan");
		button_save.setOnAction(event -> {
			pro.setVisible(true);
			FadeTransition ft = new FadeTransition();
			ft.setNode(pro);
			ft.setFromValue(0); ft.setToValue(1); ft.play();
			FileChooser fc = new FileChooser();
			fc.setTitle("Kies bestand");
			fc.getExtensionFilters().add(new ExtensionFilter(".ste", 
					"*.ste"));
			File file = fc.showSaveDialog(stage);
			if (file != null) {
				chosen_file.setText("Opgeslaan als "+
						file.getAbsolutePath());
				try {
					writeFile(file);
				} catch (Exception e) {
					chosen_file.setText("Iets ging mis.");
					e.printStackTrace();
				}
			} else {
				chosen_file.setText("Niet opgeslagen");
			}
			ft.setFromValue(1); ft.setToValue(0); ft.playFromStart();
		});
		LineChart<Number, Number> chart = new LineChart<>(new NumberAxis(),
				new NumberAxis());
		XYChart.Series<Number, Number> serie = new XYChart.Series<>();
		serie.setName("Test");
		serie.getData().add(new XYChart.Data<>(1, 20));
		serie.getData().add(new XYChart.Data<>(2, 40));
		chart.getData().add(serie);
		chart.setAnimated(true);
		controls.add(title, 0, 0);
		controls.add(pro, 2, 1);
		controls.add(button, 0, 1);
		controls.add(button_save, 0, 2);
		controls.add(chosen_file, 1, 1);
		GridPane.setColumnSpan(title, GridPane.REMAINING);
		GridPane.setColumnSpan(pro, GridPane.REMAINING);
		return controls;
	}
	
	public VBox buildGraph(Menu menu) {
		VBox box = new VBox();
		box.setOnMouseClicked(event -> {
			event.consume();
		});
		LineChart<Number, Number> chart = new LineChart<>(new NumberAxis(),
				new NumberAxis());
		for (int i : menu.items) {
			Param p = manager.getParam(i);
			XYChart.Series<Number, Number> serie = new XYChart.Series<>();
			serie.setName(p.getName());
			for (int x = 0;x < p.getValues().getSize();x++) {
				XYChart.Data<Number, Number> dat = new XYChart.Data<>();
				dat.setXValue(x);
				dat.YValueProperty().bind(p.getValues().valueAt(x));
				serie.getData().add(dat);
			}
			chart.getData().add(serie);
		}
		chart.setAnimated(true);
		box.getChildren().add(chart);
		return box;
	}

	public ScrollPane buildTableScene(Menu menu) {
		final VBox vbox = new VBox();
		final ScrollPane pane = new ScrollPane(vbox);
		final Label label = new Label(menu.name);
		label.setPadding(new Insets(10));
		label.setFont(new Font("Arial", 20));
		GridPane table = new GridPane();
		table.setPadding(new Insets(10));
		table.setHgap(10);
		pane.setEffect(new DropShadow(8, Color.BLACK));
		for (int i = 0;i < menu.items.length/*.getParams().size()*/;i++) {
			//Param p = manager.getParam(i);
			Param p = manager.getParam(menu.items[i]);
			Text t = new Text();
			t.textProperty().bind(p.nameProperty());
			table.add(t, 0, i);
			if (p instanceof ChoiceParam) {
				ChoiceBox<String> b = new ChoiceBox<>();
				b.disableProperty().bind(p.activProperty().not());
				b.setItems(((ChoiceParam)p).getOptions());
				p.getValues().addListener((a, o, n) -> {
					b.getSelectionModel().select(p.getIntValue());
				});
				b.getSelectionModel().select(p.getIntValue());
				b.getSelectionModel().selectedIndexProperty()
					.addListener((a, c, d) -> {
						manager.setParam(p, d.intValue());});
				table.add(b, 1, i);
			} else if (p instanceof IntegerParam || 
					p instanceof StringParam) {
				TextField tf = new TextField();
				tf.disableProperty().bind(p.activProperty().not());
				p.getValues().addListener((ListChangeListener.Change<? 
						extends Integer> n) -> {
					tf.setText(p.getStringValue());
				});
				tf.textProperty().addListener((a, o, n) -> {
					if (p.isValidStringValue(n))
						tf.setStyle("-fx-text-inner-color:black;");
					else tf.setStyle("-fx-text-inner-color:red;");
				});
				tf.setOnAction(event -> {
					if (p.isValidStringValue(tf.getText()))
						manager.setParam(p, tf.getText()); 
								//Integer.parseInt(tf.getText()));
				});
				table.add(tf, 1, i);
			}
		}
		vbox.setOnMouseClicked(event -> {
			event.consume();
		});
		vbox.getChildren().addAll(label, table);
		//return vbox;
		//pane.setMinSize(200, 200);
		pane.managedProperty().bind(pane.visibleProperty());
		pane.setMaxHeight(200);
		pane.setFitToWidth(true);
		return pane;
	}
	
	public void writeFile(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		for (int i = 0;i < manager.getParams().size();i++) {
			fos.write((byte) i);
			fos.write((byte) manager.getParam(i).getValues().getSize()*2);
			for (int x : manager.getParam(i).getIntValues()) {
				for (int y : ParamManager.convertToBytes(x)) {
					fos.write((byte) y);
				}
			}
		}
		fos.close();
	}
	
	public void readFile(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		DataInputStream dis = new DataInputStream(fis);
		ReadState state = ReadState.EXPECT_ID;
		int id = 0;
		int length = 0;
		int read_count = 0;
		int[] content = new int[0];
		while (dis.available() >= 1) {
			int i = dis.readUnsignedByte();
			switch (state) {
			case EXPECT_ID:
				id = i;
				state = ReadState.EXPECT_LENGTH;
				break;
			case EXPECT_LENGTH:
				length = i;
				read_count = 0;
				content = new int[length];
				if (length >= 1) state = ReadState.READING;
				else state = ReadState.EXPECT_ID;
				break;
			case READING:
				content[read_count] = i;
				read_count++;
				if (read_count == length) {
					//System.out.println("Param id="+id+" length="+length
					//		+ " content=" + Arrays.toString(content));
					System.out.println("Saving param id="+id);
					boolean r = manager.setParam(id, ParamManager.
							convertFromBytes(content));
					System.out.println("original bytes: " + 
							Arrays.toString(content));
					if (!r) System.out.println("failed");
					Param p = manager.getParam(id);
					if (p != null)
					System.out.println("returned bytes: " +
							Arrays.toString(ParamManager.convertToBytes(
									p.getIntValues())));
					state = ReadState.EXPECT_ID;
				}
				break;
			}
		}
		dis.close();
	}
	
	public enum ReadState {
		EXPECT_ID,
		EXPECT_LENGTH,
		READING
	}
}
