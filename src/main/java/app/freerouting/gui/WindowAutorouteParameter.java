package app.freerouting.gui;

import app.freerouting.board.Layer;
import app.freerouting.board.LayerStructure;
import app.freerouting.interactive.AutorouteSettings;
import app.freerouting.interactive.BoardHandling;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/** Window handling parameters of the automatic routing. */
public class WindowAutorouteParameter extends BoardSavableSubWindow {

  private final BoardHandling board_handling;
  private final JLabel[] layer_name_arr;
  private final JCheckBox[] layer_active_arr;
  private final JComboBox<String>[] combo_box_arr;
  private final JCheckBox vias_allowed;
  private final JCheckBox fanout_pass_button;
  private final JCheckBox autoroute_pass_button;
  private final JCheckBox postroute_pass_button;
  private final WindowAutorouteDetailParameter detail_window;
  private final DetailListener detail_listener;
  private final String horizontal;
  private final String vertical;
  /** Creates a new instance of WindowAutorouteParameter */
  public WindowAutorouteParameter(BoardFrame p_board_frame) {
    this.board_handling = p_board_frame.board_panel.board_handling;
    ResourceBundle resources =
        ResourceBundle.getBundle(
            "app.freerouting.gui.WindowAutorouteParameter", p_board_frame.get_locale());
    this.setTitle(resources.getString("title"));

    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    // create main panel

    final JPanel main_panel = new JPanel();
    getContentPane().add(main_panel);
    GridBagLayout gridbag = new GridBagLayout();
    main_panel.setLayout(gridbag);
    GridBagConstraints gridbag_constraints = new GridBagConstraints();
    gridbag_constraints.anchor = GridBagConstraints.WEST;
    gridbag_constraints.insets = new Insets(1, 10, 1, 10);

    gridbag_constraints.gridwidth = 3;
    JLabel layer_label = new JLabel(resources.getString("layer"));
    gridbag.setConstraints(layer_label, gridbag_constraints);
    main_panel.add(layer_label);

    JLabel active_label = new JLabel(resources.getString("active"));
    gridbag.setConstraints(active_label, gridbag_constraints);
    main_panel.add(active_label);

    gridbag_constraints.gridwidth = GridBagConstraints.REMAINDER;
    JLabel preferred_direction_label =
        new JLabel(resources.getString("preferred_direction"));
    gridbag.setConstraints(preferred_direction_label, gridbag_constraints);
    main_panel.add(preferred_direction_label);

    this.horizontal = resources.getString("horizontal");
    this.vertical = resources.getString("vertical");

    // create the layer list
    LayerStructure layer_structure =
        board_handling.get_routing_board().layer_structure;
    int layer_count = layer_structure.arr.length;

    // every layer is a row in the gridbag and has 3 columns: name, active, preferred direction
    layer_name_arr = new JLabel[layer_count];
    layer_active_arr = new JCheckBox[layer_count];
    combo_box_arr = new JComboBox[layer_count];

    for (int i = 0; i < layer_count; ++i) {
      gridbag_constraints.gridwidth = 3;
      Layer curr_layer = layer_structure.arr[i];

      // set the name
      layer_name_arr[i] = new JLabel();
      layer_name_arr[i].setText(curr_layer.name);
      gridbag.setConstraints(layer_name_arr[i], gridbag_constraints);
      main_panel.add(layer_name_arr[i]);

      // set the active checkbox
      layer_active_arr[i] = new JCheckBox();
      layer_active_arr[i].addActionListener(new LayerActiveListener(i));
      board_handling.settings.autoroute_settings.set_layer_active(i, curr_layer.is_signal);
      gridbag.setConstraints(layer_active_arr[i], gridbag_constraints);
      main_panel.add(layer_active_arr[i]);

      // set the preferred direction combobox
      combo_box_arr[i] = new JComboBox<>();
      combo_box_arr[i].addItem(this.horizontal);
      combo_box_arr[i].addItem(this.vertical);
      combo_box_arr[i].addActionListener(new PreferredDirectionListener(i));
      gridbag_constraints.gridwidth = GridBagConstraints.REMAINDER;
      gridbag.setConstraints(combo_box_arr[i], gridbag_constraints);
      main_panel.add(combo_box_arr[i]);
    }

    JLabel separator =
        new JLabel("––––––––––––––––––––––––––––––––––––––––  ");
    gridbag.setConstraints(separator, gridbag_constraints);
    main_panel.add(separator, gridbag_constraints);

    gridbag_constraints.gridwidth = GridBagConstraints.RELATIVE;
    JLabel vias_allowed_label =
        new JLabel(resources.getString("vias_allowed"));
    gridbag.setConstraints(vias_allowed_label, gridbag_constraints);
    main_panel.add(vias_allowed_label);

    this.vias_allowed = new JCheckBox();
    this.vias_allowed.addActionListener(new ViasAllowedListener());
    gridbag_constraints.gridwidth = GridBagConstraints.REMAINDER;
    gridbag.setConstraints(vias_allowed, gridbag_constraints);
    main_panel.add(vias_allowed);

    separator = new JLabel("––––––––––––––––––––––––––––––––––––––––  ");
    gridbag.setConstraints(separator, gridbag_constraints);
    main_panel.add(separator, gridbag_constraints);

    JLabel passes_label = new JLabel(resources.getString("passes"));

    gridbag_constraints.gridwidth = GridBagConstraints.RELATIVE;
    gridbag_constraints.gridheight = 3;
    gridbag.setConstraints(passes_label, gridbag_constraints);
    main_panel.add(passes_label);

    this.fanout_pass_button = new JCheckBox(resources.getString("fanout"));
    this.autoroute_pass_button = new JCheckBox(resources.getString("autoroute"));
    this.postroute_pass_button = new JCheckBox(resources.getString("postroute"));

    fanout_pass_button.addActionListener(new FanoutListener());
    autoroute_pass_button.addActionListener(new AutorouteListener());
    postroute_pass_button.addActionListener(new PostrouteListener());

    fanout_pass_button.setSelected(false);
    autoroute_pass_button.setSelected(true);
    autoroute_pass_button.setSelected(true);

    gridbag_constraints.gridwidth = GridBagConstraints.REMAINDER;
    gridbag_constraints.gridheight = 1;
    gridbag.setConstraints(fanout_pass_button, gridbag_constraints);
    main_panel.add(fanout_pass_button, gridbag_constraints);

    gridbag.setConstraints(autoroute_pass_button, gridbag_constraints);
    main_panel.add(autoroute_pass_button, gridbag_constraints);
    gridbag.setConstraints(postroute_pass_button, gridbag_constraints);
    main_panel.add(postroute_pass_button, gridbag_constraints);

    separator = new JLabel("––––––––––––––––––––––––––––––––––––––––  ");
    gridbag.setConstraints(separator, gridbag_constraints);
    main_panel.add(separator, gridbag_constraints);

    detail_window = new WindowAutorouteDetailParameter(p_board_frame);
    JButton detail_button =
        new JButton(resources.getString("detail_parameter"));
    this.detail_listener = new DetailListener();
    detail_button.addActionListener(detail_listener);
    gridbag.setConstraints(detail_button, gridbag_constraints);

    main_panel.add(detail_button);

    p_board_frame.set_context_sensitive_help(this, "WindowAutorouteParameter");

    this.refresh();
    this.pack();
    this.setResizable(false);
  }

  /** Recalculates all displayed values */
  @Override
  public void refresh() {
    AutorouteSettings settings =
        this.board_handling.settings.autoroute_settings;
    LayerStructure layer_structure =
        this.board_handling.get_routing_board().layer_structure;

    this.vias_allowed.setSelected(settings.get_vias_allowed());
    this.fanout_pass_button.setSelected(settings.get_with_fanout());
    this.autoroute_pass_button.setSelected(settings.get_with_autoroute());
    this.postroute_pass_button.setSelected(settings.get_with_postroute());

    for (int i = 0; i < layer_active_arr.length; ++i) {
      this.layer_active_arr[i].setSelected(
          settings.get_layer_active(i));
    }

    for (int i = 0; i < combo_box_arr.length; ++i) {
      if (settings.get_preferred_direction_is_horizontal(layer_structure.get_layer_no(i))) {
        this.combo_box_arr[i].setSelectedItem(this.horizontal);
      } else {
        this.combo_box_arr[i].setSelectedItem(this.vertical);
      }
    }
    this.detail_window.refresh();
  }

  @Override
  public void dispose() {
    detail_window.dispose();
    super.dispose();
  }

  @Override
  public void parent_iconified() {
    detail_window.parent_iconified();
    super.parent_iconified();
  }

  @Override
  public void parent_deiconified() {
    detail_window.parent_deiconified();
    super.parent_deiconified();
  }

  private class DetailListener implements ActionListener {

    private boolean first_time = true;

    @Override
    public void actionPerformed(ActionEvent p_evt) {
      if (first_time) {
        Point location = getLocation();
        detail_window.setLocation((int) location.getX() + 200, (int) location.getY() + 100);
        first_time = false;
      }
      detail_window.setVisible(true);
    }
  }

  private class LayerActiveListener implements ActionListener {

    private final int signal_layer_no;

    public LayerActiveListener(int p_layer_no) {
      signal_layer_no = p_layer_no;
    }

    @Override
    public void actionPerformed(ActionEvent p_evt) {
      int curr_layer_no = this.signal_layer_no;
//      int curr_layer_no =
//          board_handling.get_routing_board().layer_structure.get_layer_no(this.signal_layer_no);
      board_handling.settings.autoroute_settings.set_layer_active(
          curr_layer_no, layer_active_arr[this.signal_layer_no].isSelected());
    }
  }

  private class PreferredDirectionListener implements ActionListener {

    private final int signal_layer_no;

    public PreferredDirectionListener(int p_layer_no) {
      signal_layer_no = p_layer_no;
    }

    @Override
    public void actionPerformed(ActionEvent p_evt) {
      int curr_layer_no =
          board_handling.get_routing_board().layer_structure.get_layer_no(this.signal_layer_no);
      board_handling.settings.autoroute_settings.set_preferred_direction_is_horizontal(
          curr_layer_no, combo_box_arr[signal_layer_no].getSelectedItem() == horizontal);
    }
  }

  private class ViasAllowedListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent p_evt) {
      board_handling.settings.autoroute_settings.set_vias_allowed(vias_allowed.isSelected());
    }
  }

  private class FanoutListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent p_evt) {
      AutorouteSettings autoroute_settings =
          board_handling.settings.autoroute_settings;
      autoroute_settings.set_with_fanout(fanout_pass_button.isSelected());
      autoroute_settings.set_start_pass_no(1);
    }
  }

  private class AutorouteListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent p_evt) {
      AutorouteSettings autoroute_settings =
          board_handling.settings.autoroute_settings;
      autoroute_settings.set_with_autoroute(autoroute_pass_button.isSelected());
      autoroute_settings.set_start_pass_no(1);
    }
  }

  private class PostrouteListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent p_evt) {
      AutorouteSettings autoroute_settings =
          board_handling.settings.autoroute_settings;
      autoroute_settings.set_with_postroute(postroute_pass_button.isSelected());
      autoroute_settings.set_start_pass_no(1);
    }
  }
}
