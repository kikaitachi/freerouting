package app.freerouting.gui;

import app.freerouting.boardgraphics.CoordinateTransform;
import app.freerouting.geometry.planar.FloatPoint;
import app.freerouting.logger.FRLogger;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/** Window for interactive changing of miscellaneous display properties. */
public class WindowDisplayMisc extends BoardSavableSubWindow {

  private static final int MAX_SLIDER_VALUE = 100;
  private final BoardPanel panel;
  private final JRadioButton small_cursor_checkbox;
  private final JRadioButton big_cursor_checkbox;
  private final JRadioButton rotation_none_checkbox;
  private final JRadioButton rotation_90_degree_checkbox;
  private final JRadioButton rotation_180_degree_checkbox;
  private final JRadioButton rotation_270_degree_checkbox;
  private final JRadioButton mirror_none_checkbox;
  private final JRadioButton vertical_mirror_checkbox;
  private final JRadioButton horizontal_mirror_checkbox;
  private final JSlider auto_layer_dim_slider;
  /** Creates a new instance of DisplayMiscWindow */
  public WindowDisplayMisc(BoardFrame p_board_frame) {
    this.panel = p_board_frame.board_panel;
    ResourceBundle resources =
        ResourceBundle.getBundle(
            "app.freerouting.gui.DisplayMisc", p_board_frame.get_locale());
    this.setTitle(resources.getString("title"));

    // Create main panel

    final JPanel main_panel = new JPanel();
    getContentPane().add(main_panel);

    // Initialize gridbag layout.

    GridBagLayout gridbag = new GridBagLayout();
    main_panel.setLayout(gridbag);
    GridBagConstraints gridbag_constraints = new GridBagConstraints();
    gridbag_constraints.anchor = GridBagConstraints.WEST;
    gridbag_constraints.gridwidth = GridBagConstraints.RELATIVE;

    // add label and buttongroup for the appearance of the cross hair cursor.

    JLabel cursor_label =
        new JLabel("   " + resources.getString("cross_hair_cursor"));
    gridbag_constraints.gridwidth = GridBagConstraints.RELATIVE;
    gridbag_constraints.gridheight = 2;
    gridbag.setConstraints(cursor_label, gridbag_constraints);
    main_panel.add(cursor_label, gridbag_constraints);

    gridbag_constraints.gridwidth = GridBagConstraints.REMAINDER;
    gridbag_constraints.gridheight = 1;

    small_cursor_checkbox = new JRadioButton(resources.getString("small"));
    small_cursor_checkbox.setToolTipText(resources.getString("cursor_checkbox_tooltip"));
    small_cursor_checkbox.addActionListener(new SmallCursorListener());
    gridbag.setConstraints(small_cursor_checkbox, gridbag_constraints);
    main_panel.add(small_cursor_checkbox, gridbag_constraints);

    big_cursor_checkbox = new JRadioButton(resources.getString("big"));
    big_cursor_checkbox.addActionListener(new BigCursorListener());
    big_cursor_checkbox.setToolTipText(resources.getString("cursor_checkbox_tooltip"));
    gridbag.setConstraints(big_cursor_checkbox, gridbag_constraints);
    main_panel.add(big_cursor_checkbox, gridbag_constraints);

    ButtonGroup cursor_button_group = new ButtonGroup();
    cursor_button_group.add(small_cursor_checkbox);
    cursor_button_group.add(big_cursor_checkbox);

    JLabel separator =
        new JLabel("  ––––––––––––––––––––––––––––––––––––––––  ");
    gridbag.setConstraints(separator, gridbag_constraints);
    main_panel.add(separator, gridbag_constraints);

    // Add label and buttongroup for the rotation of the board.

    JLabel rotation_label =
        new JLabel("   " + resources.getString("rotation"));
    gridbag_constraints.gridwidth = GridBagConstraints.RELATIVE;
    gridbag_constraints.gridheight = 4;
    gridbag.setConstraints(rotation_label, gridbag_constraints);
    main_panel.add(rotation_label, gridbag_constraints);

    gridbag_constraints.gridwidth = GridBagConstraints.REMAINDER;
    gridbag_constraints.gridheight = 1;

    rotation_none_checkbox = new JRadioButton(resources.getString("none"));
    gridbag.setConstraints(rotation_none_checkbox, gridbag_constraints);
    main_panel.add(rotation_none_checkbox, gridbag_constraints);

    rotation_90_degree_checkbox = new JRadioButton(resources.getString("90_degree"));
    gridbag.setConstraints(rotation_90_degree_checkbox, gridbag_constraints);
    main_panel.add(rotation_90_degree_checkbox, gridbag_constraints);

    rotation_180_degree_checkbox = new JRadioButton(resources.getString("180_degree"));
    gridbag.setConstraints(rotation_180_degree_checkbox, gridbag_constraints);
    main_panel.add(rotation_180_degree_checkbox, gridbag_constraints);

    rotation_270_degree_checkbox = new JRadioButton(resources.getString("-90_degree"));
    gridbag.setConstraints(rotation_270_degree_checkbox, gridbag_constraints);
    main_panel.add(rotation_270_degree_checkbox, gridbag_constraints);

    ButtonGroup rotation_button_group = new ButtonGroup();
    rotation_button_group.add(rotation_none_checkbox);
    rotation_button_group.add(rotation_90_degree_checkbox);
    rotation_button_group.add(rotation_180_degree_checkbox);
    rotation_button_group.add(rotation_270_degree_checkbox);

    rotation_none_checkbox.addActionListener(new RotationNoneListener());
    rotation_90_degree_checkbox.addActionListener(new Rotation90Listener());
    rotation_180_degree_checkbox.addActionListener(new Rotation180Listener());
    rotation_270_degree_checkbox.addActionListener(new Rotation270Listener());

    separator = new JLabel("  ––––––––––––––––––––––––––––––––––––––––  ");
    gridbag.setConstraints(separator, gridbag_constraints);
    main_panel.add(separator, gridbag_constraints);

    // add label and buttongroup for the mirroring of the board.

    JLabel mirroring_label =
        new JLabel("   " + resources.getString("board_mirroring"));
    gridbag_constraints.gridwidth = GridBagConstraints.RELATIVE;
    gridbag_constraints.gridheight = 3;
    gridbag.setConstraints(mirroring_label, gridbag_constraints);
    main_panel.add(mirroring_label, gridbag_constraints);

    gridbag_constraints.gridwidth = GridBagConstraints.REMAINDER;
    gridbag_constraints.gridheight = 1;

    mirror_none_checkbox = new JRadioButton(resources.getString("none"));
    mirror_none_checkbox.addActionListener(new MirrorNoneListener());
    gridbag.setConstraints(mirror_none_checkbox, gridbag_constraints);
    main_panel.add(mirror_none_checkbox, gridbag_constraints);

    vertical_mirror_checkbox = new JRadioButton(resources.getString("left_right"));
    vertical_mirror_checkbox.addActionListener(new VerticalMirrorListener());
    gridbag.setConstraints(vertical_mirror_checkbox, gridbag_constraints);
    main_panel.add(vertical_mirror_checkbox, gridbag_constraints);

    horizontal_mirror_checkbox = new JRadioButton(resources.getString("top_bottom"));
    horizontal_mirror_checkbox.addActionListener(new HorizontalMirrorListener());
    gridbag.setConstraints(horizontal_mirror_checkbox, gridbag_constraints);
    main_panel.add(horizontal_mirror_checkbox, gridbag_constraints);

    ButtonGroup mirroring_button_group = new ButtonGroup();
    mirroring_button_group.add(mirror_none_checkbox);
    mirroring_button_group.add(vertical_mirror_checkbox);
    mirroring_button_group.add(horizontal_mirror_checkbox);

    separator = new JLabel("  ––––––––––––––––––––––––––––––––––––––––  ");
    gridbag.setConstraints(separator, gridbag_constraints);
    main_panel.add(separator, gridbag_constraints);

    // add slider for automatic layer dimming

    gridbag_constraints.insets = new Insets(5, 10, 5, 10);
    JLabel auto_layer_dim_label =
        new JLabel(resources.getString("layer_dimming"));
    auto_layer_dim_label.setToolTipText(resources.getString("layer_dimming_tooltip"));
    gridbag.setConstraints(auto_layer_dim_label, gridbag_constraints);
    main_panel.add(auto_layer_dim_label);
    this.auto_layer_dim_slider = new JSlider(0, MAX_SLIDER_VALUE);
    gridbag.setConstraints(auto_layer_dim_slider, gridbag_constraints);
    main_panel.add(auto_layer_dim_slider);
    this.auto_layer_dim_slider.addChangeListener(new SliderChangeListener());

    p_board_frame.set_context_sensitive_help(this, "WindowDisplay_Miscellaneous");

    this.refresh();
    this.pack();
    this.setResizable(false);
  }

  /** Refreshes the displayed values in this window. */
  @Override
  public void refresh() {
    small_cursor_checkbox.setSelected(!panel.is_custom_cross_hair_cursor());
    big_cursor_checkbox.setSelected(panel.is_custom_cross_hair_cursor());

    int ninety_degree_rotation =
        panel.board_handling.graphics_context.coordinate_transform.get_90_degree_rotation();

    switch (ninety_degree_rotation) {
      case 0 -> rotation_none_checkbox.setSelected(true);
      case 1 -> rotation_90_degree_checkbox.setSelected(true);
      case 2 -> rotation_180_degree_checkbox.setSelected(true);
      case 3 -> rotation_270_degree_checkbox.setSelected(true);
      default -> {
        FRLogger.warn("DisplayMiscWindow: unexpected ninety_degree_rotation");
        rotation_none_checkbox.setSelected(true);
      }
    }

    boolean is_mirror_left_right =
        panel.board_handling.graphics_context.coordinate_transform.is_mirror_left_right();
    boolean is_mirror_top_button =
        panel.board_handling.graphics_context.coordinate_transform.is_mirror_top_bottom();
    mirror_none_checkbox.setSelected(!(is_mirror_left_right || is_mirror_top_button));

    vertical_mirror_checkbox.setSelected(
        panel.board_handling.graphics_context.coordinate_transform.is_mirror_left_right());
    horizontal_mirror_checkbox.setSelected(
        panel.board_handling.graphics_context.coordinate_transform.is_mirror_top_bottom());

    int curr_slider_value =
        (int)
            Math.round(
                MAX_SLIDER_VALUE
                    * (1 - panel.board_handling.graphics_context.get_auto_layer_dim_factor()));
    auto_layer_dim_slider.setValue(curr_slider_value);
  }

  private class SmallCursorListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent p_evt) {
      panel.set_custom_crosshair_cursor(false);
    }
  }

  private class BigCursorListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent p_evt) {
      panel.set_custom_crosshair_cursor(true);
    }
  }

  private class RotationNoneListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent p_evt) {
      CoordinateTransform coordinate_transform =
          panel.board_handling.graphics_context.coordinate_transform;
      coordinate_transform.set_rotation(0);
      panel.repaint();
    }
  }

  private class Rotation90Listener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent p_evt) {
      CoordinateTransform coordinate_transform =
          panel.board_handling.graphics_context.coordinate_transform;
      coordinate_transform.set_rotation(0.5 * Math.PI);
      panel.repaint();
    }
  }

  private class Rotation180Listener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent p_evt) {
      CoordinateTransform coordinate_transform =
          panel.board_handling.graphics_context.coordinate_transform;
      coordinate_transform.set_rotation(Math.PI);
      panel.repaint();
    }
  }

  private class Rotation270Listener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent p_evt) {
      CoordinateTransform coordinate_transform =
          panel.board_handling.graphics_context.coordinate_transform;
      coordinate_transform.set_rotation(1.5 * Math.PI);
      panel.repaint();
    }
  }

  private class MirrorNoneListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent p_evt) {
      CoordinateTransform coordinate_transform =
          panel.board_handling.graphics_context.coordinate_transform;
      if (!(coordinate_transform.is_mirror_left_right()
          || coordinate_transform.is_mirror_top_bottom())) {
        return; // mirroring already switched off
      }
      // remember the old viewort center to retain the displayed section of the board.
      FloatPoint old_viewport_center =
          coordinate_transform.screen_to_board(panel.get_viewport_center());
      coordinate_transform.set_mirror_left_right(false);
      coordinate_transform.set_mirror_top_bottom(false);
      panel.set_viewport_center(coordinate_transform.board_to_screen(old_viewport_center));
      panel.repaint();
    }
  }

  private class VerticalMirrorListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent p_evt) {
      CoordinateTransform coordinate_transform =
          panel.board_handling.graphics_context.coordinate_transform;
      if (coordinate_transform.is_mirror_left_right()) {
        return; // already mirrored
      }
      // remember the old viewport center to retain the displayed section of the board.
      FloatPoint old_viewport_center =
          coordinate_transform.screen_to_board(panel.get_viewport_center());
      coordinate_transform.set_mirror_left_right(true);
      coordinate_transform.set_mirror_top_bottom(false);
      panel.set_viewport_center(coordinate_transform.board_to_screen(old_viewport_center));
      panel.repaint();
    }
  }

  private class HorizontalMirrorListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent p_evt) {
      CoordinateTransform coordinate_transform =
          panel.board_handling.graphics_context.coordinate_transform;
      if (coordinate_transform.is_mirror_top_bottom()) {
        return; // already mirrored
      }
      // remember the old viewport center to retain the displayed section of the board.
      FloatPoint old_viewport_center =
          coordinate_transform.screen_to_board(panel.get_viewport_center());
      coordinate_transform.set_mirror_top_bottom(true);
      coordinate_transform.set_mirror_left_right(false);
      panel.set_viewport_center(coordinate_transform.board_to_screen(old_viewport_center));
      panel.repaint();
    }
  }

  private class SliderChangeListener implements ChangeListener {
    @Override
    public void stateChanged(ChangeEvent evt) {
      double new_value = 1 - (double) auto_layer_dim_slider.getValue() / (double) MAX_SLIDER_VALUE;
      panel.board_handling.graphics_context.set_auto_layer_dim_factor(new_value);
      panel.repaint();
    }
  }
}
