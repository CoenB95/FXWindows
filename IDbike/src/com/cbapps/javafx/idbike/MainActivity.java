package com.cbapps.javafx.idbike;

import com.cbapps.javafx.fxlib.LayoutTransition;
import com.cbapps.javafx.idbike.SmoothInterpolator.AnimType;
import com.cbapps.javafx.idbike.model.param.ParamManager;

import javafx.animation.Animation.Status;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainActivity extends Application implements EventHandler<ActionEvent> {

	private NextGenBicycleScene scene_b;
	MenuItem visibleItem;
	private Button back;
	private TilePane grid;
	
	@Override
	public void start(Stage primaryStage) {
		Font largeFont = Font.loadFont(NextGenBicycleScene.class
				.getResourceAsStream("resources/Roboto-Thin.ttf"), 30);
		Scene scene = new Scene(new Group(), 800, 600);
		BorderPane border = new BorderPane();
		StackPane stack = new StackPane();
		scene_b = new NextGenBicycleScene();
		grid = new TilePane();
		back = new Button("Terug");
		back.defaultButtonProperty().bind(back.focusedProperty());
		grid.setAlignment(Pos.CENTER);
		back.setVisible(false);
		back.setOnAction(event -> {
			back.setVisible(false);
			grid.setDisable(false);
			if (visibleItem != null) visibleItem.schrink();
			event.consume();
		});
		stack.setOnMouseClicked(event -> {
			back.setVisible(false);
			grid.setDisable(false);
			if (visibleItem != null) visibleItem.schrink();
			event.consume();
		});
		GridPane bottom = scene_b.buildControls(primaryStage);
		MenuItem[] menus = new MenuItem[ParamManager.ALL_MENUS.length+1];
		for (int i = 0;i < ParamManager.ALL_MENUS.length;i++) {
			StackPane menu_node = new StackPane();
			menu_node.getChildren().add(new Group(
					scene_b.buildTableScene(ParamManager
							.ALL_MENUS[i])));
			//menu_node.prefHeightProperty().bind(stack.heightProperty());
			menu_node.setVisible(false);
			stack.getChildren().add(menu_node);
			menus[i] = new MenuItem(ParamManager.ALL_MENUS[i].name,
					largeFont, menu_node, this);
			LayoutTransition.setup(menus[i]);
		}
		VBox graph = scene_b.buildGraph(ParamManager.GRAPH_MENU);
		graph.setVisible(false);
		stack.getChildren().add(graph);
		menus[ParamManager.ALL_MENUS.length] = new MenuItem(ParamManager
				.GRAPH_MENU.name, largeFont, graph, this);
		LayoutTransition.setup(menus[ParamManager.ALL_MENUS.length]);
		grid.setOrientation(Orientation.HORIZONTAL);
		grid.setPrefColumns(2);
		grid.getChildren().addAll(menus);
		stack.getChildren().addAll(grid);
		border.setTop(back);
		border.setCenter(stack);
		border.setBottom(bottom);
		scene.setRoot(border);
		scene.getStylesheets().add("/com/cbapps/javafx/idbike/stylesheets/"
				+ "CleanTheme.css");
		primaryStage.setTitle("IDbike parameters");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static class MenuItem extends Button {
		
		private TranslateTransition tt;
		private ScaleTransition st;
		private FadeTransition ft;
		private Region node;
		private boolean schrinking = false;
		
		public MenuItem(String name, Font font, Region n, 
				EventHandler<ActionEvent> handler) {
			node = n;
//			node.setDisable(true);
//			node.setOpacity(0);
//			node.setVisible(true);
			setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			setText(name);
			setFont(font);
			setPadding(new Insets(25));
			setOnAction(handler);
			defaultButtonProperty().bind(focusedProperty());
			tt = new TranslateTransition();
			st = new ScaleTransition();
			ft = new FadeTransition();
		}
		
		public void grow() {
			node.toFront();
			Rectangle rect = new Rectangle(
					node.getWidth(), node.getHeight());
			rect.setX(0);
			rect.setY(0);
			node.setClip(rect);
			node.setVisible(true);
			node.setOpacity(1);
			tt = new TranslateTransition();
			tt.setNode(node);
			tt.setDuration(new Duration(600));
			tt.setInterpolator(new SmoothInterpolator(AnimType.DECELERATE));
			tt.setFromX(getLayoutX() + (getWidth()/2) -
					(node.getLayoutX() + node.getWidth()/2));
			tt.setFromY(getLayoutY() + (getHeight()/2) -
					(node.getLayoutY() + node.getHeight()/2));
			tt.setToX(0);
			tt.setToY(0);
			st = new ScaleTransition();
			st.setNode(rect);
			st.setDuration(new Duration(600));
			st.setInterpolator(new SmoothInterpolator(AnimType.DECELERATE));
			st.setFromX(0);
			st.setFromY(0);
			st.setToX(1);
			st.setToY(1);
			tt.playFromStart();
			st.playFromStart();
		}
		
		public void schrink() {
			if (schrinking) return;
			schrinking = true;
			ft = new FadeTransition();
			ft.setNode(node);
			ft.setFromValue(1);
			ft.setToValue(0);
			ft.setOnFinished(event -> {
				node.setVisible(false);
				schrinking = false;
			});
			if ((tt.statusProperty().get() == Status.RUNNING)) 
				ft.setDelay(tt.getDuration().subtract(tt.getCurrentTime()));
			ft.play();
		}
	}

	@Override
	public void handle(ActionEvent event) {
		visibleItem = (MenuItem) event.getSource();
		grid.setDisable(true);
		back.setVisible(true);
		visibleItem.grow();
	}
}