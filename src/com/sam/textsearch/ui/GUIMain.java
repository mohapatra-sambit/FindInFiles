package com.sam.textsearch.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.sam.textsearch.bean.InputBean;
import com.sam.textsearch.bean.StatusBean;
import com.sam.textsearch.excp.KBException;
import com.sam.textsearch.init.Loader;
import com.sam.textsearch.mgr.TSManager;
import com.sam.textsearch.model.ResultTableModel;
import com.sam.textsearch.model.SavedSearchModel;
import com.sam.textsearch.stats.Statistics;
import com.sam.textsearch.utils.Constants;
import com.sam.textsearch.utils.FileUtils;
import com.sam.textsearch.utils.GenUtils;

public class GUIMain extends JFrame {
	
	private static final Logger log = Logger.getLogger(GUIMain.class);

	private static final long serialVersionUID = -5507965424380188809L;
	
	private JLabel enterSearchKeywordLabel;
	
	private JLabel locationLabel;
	
	private JLabel filterLabel;
	
	private JLabel currentStatusLabel;
	
	private JLabel loadingImageLabel;
	
	private JLabel loadSearchLabel;
	
	private JTextField searchKeywordTextField;
	
	private JTextField locationTextField;
	
	private JTextField filterTextField;
	
	private JButton browseButton;
	
	private JButton searchButton;
	
	private JButton resetButton;
	
	private JButton saveButton;
	
	private JButton exitButton;
	
	private JButton stopButton;
	
	private JComboBox loadSearchComboBox;
	
	private JTable resultTable;
	
	private JPanel informationPanel;
	
	private SwingWorker<Void, String> w;
	
	public GUIMain() {
		super("Find in Files");
		log.debug("[GUIMain {GUIMain}] :: Creating UI components.");
		createComponents();
		log.debug("[GUIMain {GUIMain}] :: Arranging UI components.");
		arrangeComponentsInFrame();
		searchKeywordTextField.requestFocus();
		log.debug("[GUIMain {GUIMain}] :: Setting frame properties.");
		loadFrameProperties();
	}

	private void loadFrameProperties() {
        InputStream icon16 = this.getClass().getResourceAsStream(
        		Constants.FIF_ICON_SMALL_PATH);
        InputStream icon32 = this.getClass().getResourceAsStream(
        		Constants.FIF_ICON_BIG_PATH);
        final List<Image> icons = new ArrayList<Image>();
        try {
			icons.add(ImageIO.read(icon16));
			icons.add(ImageIO.read(icon32));
		} catch (IOException e) {
			e.printStackTrace();
		}
        this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowOpened(WindowEvent e) {
				checkSavedSearch();
			}

			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				close();
			}
        	
		});
        this.setIconImages(icons);
		this.setSize(new Dimension(1200, 650));
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void createComponents() {
		log.debug("[GUIMain {createComponents}] :: Creating input components.");
		createInputComponents();
		log.debug("[GUIMain {createComponents}] :: Creating result components.");
		createResultComponents();
		log.debug("[GUIMain {createComponents}] :: Creating information components.");
		createInformationComponents();
	}

	private void createInputComponents() {
		enterSearchKeywordLabel = new JLabel(Constants.KEYWORD_LABEL + " :");
		enterSearchKeywordLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
		enterSearchKeywordLabel.setVerticalAlignment(JLabel.CENTER);
		enterSearchKeywordLabel.setHorizontalAlignment(JLabel.RIGHT);
		enterSearchKeywordLabel.setPreferredSize(new Dimension(200,20));
		
		searchKeywordTextField = new JTextField(75);
		searchKeywordTextField.setToolTipText(Constants.KEYWORD_TOOL_TIP);
		searchKeywordTextField.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				InputBean.setKeyword(searchKeywordTextField.getText());
			}

			@Override
			public void focusGained(FocusEvent e) {
				currentStatusLabel.setText("Please enter the keyword to search here.");
			}
			
		});
		
		locationLabel = new JLabel(Constants.LOCATION_LABEL + " :");
		locationLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
		locationLabel.setVerticalAlignment(JLabel.CENTER);
		locationLabel.setHorizontalAlignment(JLabel.RIGHT);
		locationLabel.setPreferredSize(new Dimension(200,20));
		
		locationTextField = new JTextField(75);
		locationTextField.setEditable(false);
		locationTextField.setBackground(Color.WHITE);
		locationTextField.setForeground(Color.DARK_GRAY);
		locationTextField.setToolTipText(Constants.LOCATION_TOOL_TIP);
		locationTextField.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				browse();
			}
			
		});
		locationTextField.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				currentStatusLabel.setText("Please select the search location here.");
			}
			
		});
		
		filterLabel = new JLabel(Constants.FILTERS_LABEL + " :");
		filterLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
		filterLabel.setVerticalAlignment(JLabel.CENTER);
		filterLabel.setHorizontalAlignment(JLabel.RIGHT);
		filterLabel.setPreferredSize(new Dimension(200,20));
		
		filterTextField = new JTextField(75);
		filterTextField.setToolTipText(Constants.FILTERS_TOOL_TIP);
		filterTextField.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				InputBean.setFilters(filterTextField.getText());
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				currentStatusLabel.setText("Please specify the extensions of the files for " +
						"searching here as comma-separated values.");
			}
			
		});
		
		browseButton = new JButton(Constants.BROWSE_BUTTON_LABEL);
		browseButton.setPreferredSize(new Dimension(80,20));
		browseButton.setMnemonic(KeyEvent.VK_B);
		browseButton.setToolTipText(Constants.BROWSE_BUTTON_TOOL_TIP);
		browseButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				browse();
			}
		
		});
		
		searchButton = new JButton(Constants.SEARCH_BUTTON_LABEL);
		searchButton.setMnemonic(KeyEvent.VK_S);
		searchButton.setToolTipText(Constants.SEARCH_BUTTON_TOOL_TIP);
		searchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					log.debug("[GUIMain {searchButton:ActionListener}] :: Performing pre-search activities.");
					preSearch();
					log.debug("[GUIMain {searchButton:ActionListener}] :: Performing search.");
					search();
				} catch (KBException kbe) {
					log.error("[GUIMain {preSearch}] :: Error occurred while validating the inputs", kbe);
					StatusBean.addErrorMsg("Error occurred while validating the inputs.",
							kbe.getMessage(), KBException.getException(kbe));
					JOptionPane.showMessageDialog(null, kbe.getErrorMessage(),
							kbe.getCode().getErrorLevel().getLevel(), JOptionPane.ERROR_MESSAGE);
				} finally {
					postSearch();
				}
			}

		});
		
		resetButton = new JButton(Constants.RESET_BUTTON_LABEL);
		resetButton.setMnemonic(KeyEvent.VK_R);
		resetButton.setToolTipText(Constants.RESET_BUTTON_TOOL_TIP);
		resetButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
			}

		});
		
		saveButton = new JButton(Constants.SAVE_BUTTON_LABEL);
		saveButton.setMnemonic(KeyEvent.VK_A);
		saveButton.setToolTipText(Constants.SAVE_BUTTON_TOOL_TIP);
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}

		});
		
		loadSearchLabel = new JLabel(Constants.LOAD_SEARCH_LABEL);
		loadSearchLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
		loadSearchLabel.setVerticalAlignment(JLabel.CENTER);
		loadSearchLabel.setHorizontalAlignment(JLabel.RIGHT);
		loadSearchLabel.setPreferredSize(new Dimension(250, 15));
		
		loadSearchComboBox = new JComboBox(SavedSearchModel.getInstance());
		loadSearchComboBox.setEditable(false);
		loadSearchComboBox.setToolTipText(Constants.LOAD_SEARCH_COMBO_BOX_TOOL_TIP);
		loadSearchComboBox.setSelectedIndex(-1);
		loadSearchComboBox.setPreferredSize(new Dimension(150, 20));
		loadSearchComboBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(loadSearchComboBox.getSelectedIndex() > -1) {
					load();
				}
			}
		
		});
		
		exitButton = new JButton(Constants.EXIT_BUTTON_LABEL);
		exitButton.setMnemonic(KeyEvent.VK_X);
		exitButton.setToolTipText(Constants.EXIT_BUTTON_TOOL_TIP);
		exitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				GUIMain.this.dispose();
			}

		});

		stopButton = new JButton(Constants.STOP_BUTTON_LABEL);
		stopButton.setMnemonic(KeyEvent.VK_T);
		stopButton.setToolTipText(Constants.STOP_BUTTON_TOOL_TIP);
		stopButton.setEnabled(false);
		stopButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TSManager.stop();
				w.cancel(true);
				InputBean.reset();
				Statistics.reset();
				ResultTableModel.getInstance().reset();
				currentStatusLabel.setText("Please provide the above information and click on Search.");
			}

		});

	}

	private void createResultComponents() {
		resultTable = new JTable(ResultTableModel.getInstance());
		resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		resultTable.getTableHeader().setResizingAllowed(false);
		resultTable.setRowSelectionAllowed(false);
		resultTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		resultTable.setCellSelectionEnabled(true);

		ResultTableRenderer renderer = new ResultTableRenderer();
		
		TableColumn c = resultTable.getColumnModel().getColumn(0);
		c.setPreferredWidth(50);
		c.setCellRenderer(renderer);
		
		c = resultTable.getColumnModel().getColumn(1);
		c.setPreferredWidth(1075);
		c.setCellRenderer(renderer);
		
		c = resultTable.getColumnModel().getColumn(2);
		c.setPreferredWidth(50);
		c.setCellRenderer(renderer);
		
		resultTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if(e.getClickCount() > 1) {
						int x = resultTable.getSelectedColumn();
						int y = resultTable.getSelectedRow();
						String item = (String) resultTable.getValueAt(y, x);
						open(item);
					}
				}
			}
			
		});
	}
	
	private void createInformationComponents() {
		currentStatusLabel = new JLabel("Please provide the above information and click on Search.");
		currentStatusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		currentStatusLabel.setVerticalAlignment(JLabel.CENTER);
		currentStatusLabel.setHorizontalAlignment(JLabel.CENTER);
		currentStatusLabel.setPreferredSize(new Dimension(1150,20));
		currentStatusLabel.setBorder(new TitledBorder(""));
		
		URL url = this.getClass().getClassLoader().getResource(Constants.LOADER_IMAGE_PATH);
		loadingImageLabel = new JLabel(new ImageIcon(url));
		loadingImageLabel.setVisible(false);
	}
	
	private void arrangeComponentsInFrame() {
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());
		log.debug("[GUIMain {arrangeComponentsInFrame}] :: Arranging input components.");
		arrangeInputsComponents(c);
		log.debug("[GUIMain {arrangeComponentsInFrame}] :: Arranging result components.");
		arrangeResultsComponents(c);
		log.debug("[GUIMain {arrangeComponentsInFrame}] :: Arranging information components.");
		arrangeInformationComponents(c);
	}
	
	private void arrangeInputsComponents(Container c) {
		JPanel inputPanel = new JPanel();
		inputPanel.setBorder(new TitledBorder(Constants.SEARCH_INPUTS_TITLE));
		inputPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbCons = new GridBagConstraints();
		gbCons.insets = new Insets(0, 0, 5, 0);
		addComponent(inputPanel, enterSearchKeywordLabel, 0, 0, gbCons);
		addComponent(inputPanel, searchKeywordTextField, 0, 1, gbCons);
		addComponent(inputPanel, locationLabel, 1, 0, gbCons);
		addComponent(inputPanel, locationTextField, 1, 1, gbCons);
		gbCons.insets = new Insets(0, 5, 5, 0);
		addComponent(inputPanel, browseButton, 1, 2, gbCons);
		gbCons.insets = new Insets(0, 0, 10, 0);
		addComponent(inputPanel, filterLabel, 2, 0, gbCons);
		addComponent(inputPanel, filterTextField, 2, 1, gbCons);
		gbCons.gridwidth = 3;
		JPanel opsPanel = new JPanel();
		arrangeOpsPanel(opsPanel);
		gbCons.insets = new Insets(0, 0, 5, 0);
		addComponent(inputPanel, opsPanel, 3, 0, gbCons);
		c.add(inputPanel, BorderLayout.NORTH);
	}

	private void arrangeOpsPanel(JPanel opsPanel) {
		opsPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbCons = new GridBagConstraints();
		gbCons.insets = new Insets(0, 0, 0, 0);
		addComponent(opsPanel, loadSearchLabel, 0, 0, gbCons);
		gbCons.insets = new Insets(0, 0, 0, 10);
		addComponent(opsPanel, loadSearchComboBox, 0, 1, gbCons);
		gbCons.insets = new Insets(0, 0, 0, 50);
		addComponent(opsPanel, saveButton, 0, 2, gbCons);
		gbCons.insets = new Insets(0, 0, 0, 5);
		addComponent(opsPanel, searchButton, 0, 3, gbCons);
		addComponent(opsPanel, stopButton, 0, 4, gbCons);
		gbCons.insets = new Insets(0, 0, 0, 50);
		addComponent(opsPanel, resetButton, 0, 5, gbCons);
		gbCons.insets = new Insets(0, 0, 0, 0);
		addComponent(opsPanel, exitButton, 0, 6, gbCons);
	}

	private void arrangeResultsComponents(Container c) {
		c.add(new JScrollPane(resultTable), BorderLayout.CENTER);
	}
	
	private void arrangeInformationComponents(Container c) {
		informationPanel = new JPanel();
		informationPanel.add(loadingImageLabel);
		informationPanel.add(currentStatusLabel);
		c.add(informationPanel, BorderLayout.SOUTH);
	}
	
	private void addComponent(Container c, Component comp, int x, int y,
			GridBagConstraints gbc) {
		gbc.gridx = y;
		gbc.gridy = x;
		c.add(comp, gbc);
	}

	private void browse() {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jfc.setMultiSelectionEnabled(false);
		String currentPath = locationTextField.getText();
		if (currentPath != null && currentPath.trim().length() > 0) {
			File f = new File(currentPath);
			if (f.exists()) {
				jfc.setSelectedFile(f);
			}
		}
		int retval = jfc.showOpenDialog(this);
		if (retval == JFileChooser.APPROVE_OPTION) {
			File selectedFileOrFolder = jfc.getSelectedFile();
			locationTextField.setText(selectedFileOrFolder.getAbsolutePath());
			InputBean.setLocation(selectedFileOrFolder);
		}
	}
	
	private void reset() {
		stopButton.setEnabled(false);
		searchKeywordTextField.setText("");
		locationTextField.setText("");
		filterTextField.setText("");
		browseButton.setEnabled(true);
		searchButton.setEnabled(true);
		saveButton.setEnabled(true);
		checkSavedSearch();
		loadSearchComboBox.setSelectedIndex(-1);
		exitButton.setEnabled(true);
		searchKeywordTextField.setEnabled(true);
		locationTextField.setEnabled(true);
		filterTextField.setEnabled(true);
		InputBean.reset();
		Statistics.reset();
		ResultTableModel.getInstance().reset();
		currentStatusLabel.setText("Please provide the above information and click on Search.");
		loadingImageLabel.setVisible(false);
	}
	
	private void search() {
		w = new SwingWorker<Void, String>() {
			
			private TSManager mgr;

			@Override
			protected Void doInBackground() throws Exception {
				mgr = new TSManager();
				log.debug("[GUIMain {search:SwingWorker:doInBackground}] :: Calling search manager.");
				mgr.searchText(GUIMain.this);
				return null;
			}

			@Override
			protected void done() {
				mgr = null;
				log.debug("[GUIMain {search:SwingWorker:done}] :: Performing post search activities.");
				postSearch();
			}
			
		};
		/*try {
			SwingUtilities.invokeAndWait(w);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		w.execute();
	}
	
	private void preSearch() throws KBException {
		//try {
			Statistics.reset();
			stopButton.setEnabled(true);
			currentStatusLabel.setText("Looking for specified files...");
			resultTable.setEnabled(false);
			browseButton.setEnabled(false);
			searchButton.setEnabled(false);
			resetButton.setEnabled(false);
			saveButton.setEnabled(false);
			loadSearchComboBox.setEnabled(false);
			exitButton.setEnabled(false);
			searchKeywordTextField.setEnabled(false);
			locationTextField.setEnabled(false);
			filterTextField.setEnabled(false);
			InputBean.setKeyword(searchKeywordTextField.getText());
			InputBean.setFilters(filterTextField.getText());
			ResultTableModel.getInstance().reset();
			loadingImageLabel.setVisible(true);
			InputBean.validate();
		/*} catch (KBException tse) {
			log.error("[GUIMain {preSearch}] :: Error occurred while validating the inputs", tse);
			StatusBean.addErrorMsg("Error occurred while validating the inputs.",
					tse.getMessage(), KBException.getException(tse));
			JOptionPane.showMessageDialog(null, tse.getErrorMessage(),
					tse.getCode().getErrorLevel().getLevel(), JOptionPane.ERROR_MESSAGE);
		}*/
	}
	
	private void postSearch() {
		stopButton.setEnabled(false);
		resultTable.setEnabled(true);
		browseButton.setEnabled(true);
		searchButton.setEnabled(true);
		resetButton.setEnabled(true);
		saveButton.setEnabled(true);
		loadSearchComboBox.setEnabled(true);
		exitButton.setEnabled(true);
		searchKeywordTextField.setEnabled(true);
		locationTextField.setEnabled(true);
		filterTextField.setEnabled(true);
		loadingImageLabel.setVisible(false);
	}
	
	private void close() {
		if (StatusBean.isNewSavedSearchedAdded()) {
			FileUtils.saveSearchInFile();
		}
	}

	private void save() {
		try {
			log.debug("[GUIMain {save}] :: Calling IB validate.");
			InputBean.validate();
			log.debug("[GUIMain {save}] :: Calling Loader check save criteria");
			Loader.checkSaveCriteria();
			String name = JOptionPane.showInputDialog(null, Constants.INPUT_SAVE_SEARCH_LABEL,
					Constants.INPUT_SAVE_SEARCH_TITLE, JOptionPane.QUESTION_MESSAGE);
			log.debug("[GUIMain {save}] :: User entered name for saving the search is [" + name + "].");
			Loader.addSavedSearch(name);
			SavedSearchModel ssm = SavedSearchModel.getInstance();
			ssm.addItem(name);
			JOptionPane.showMessageDialog(null, Constants.SAVE_SEARCH_CONFIRMATION_MSG,
					Constants.INPUT_SAVE_SEARCH_TITLE, JOptionPane.PLAIN_MESSAGE);
			loadSearchComboBox.setEnabled(true);
			log.debug("[GUIMain {save}] :: Search is saved.");
		} catch (KBException e) {
			log.error("[GUIMain {save}] :: Error occurred while saving the search.", e);
			StatusBean.addErrorMsg("Error occurred while saving the search.",
					e.getMessage(), KBException.getException(e));
			JOptionPane.showMessageDialog(null, e.getErrorMessage(),
					e.getCode().getErrorLevel().getLevel(), JOptionPane.ERROR_MESSAGE);
		}
		
	} 
	
	private void load() {
		try {
			searchKeywordTextField.setText("");
			locationTextField.setText("");
			filterTextField.setText("");
			String name = (String) SavedSearchModel.getInstance().getSelectedItem();
			log.debug("[GUIMain {load}] :: Loading search criteria for [" + name + "].");
			String allDetails = Loader.getSavedSearchDetails(name);
			log.debug("[GUIMain {load}] :: Saved search details: " + allDetails);
			String[] details = allDetails.split(",");
			String keyword = GenUtils.revertComma(details[0]);
			String location = GenUtils.revertComma(details[1]);
			searchKeywordTextField.setText(keyword);
			InputBean.setKeyword(keyword);
			locationTextField.setText(location);
			InputBean.setLocation(new File(location));
			if (details.length > 2) {
				String filters = GenUtils.revertComma(details[2]);
				filterTextField.setText(filters);
				InputBean.setFilters(filters);
			}
			log.debug("[GUIMain {load}] :: Search is loaded.");
		} catch (KBException e) {
			log.error("[GUIMain {load}] :: Error occurred while loading the search.", e);
			StatusBean.addErrorMsg("Error occurred while loading the saved search.",
					e.getMessage(), KBException.getException(e));
			JOptionPane.showMessageDialog(null, e.getErrorMessage(),
					e.getCode().getErrorLevel().getLevel(), JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	private void open(String file) {
		try {
			log.debug("[GUIMain {open}] :: Opening file [" + file + "]");
			Desktop.getDesktop().open(new File(file));
		} catch (IOException e) {
			log.error("Error occurred while opening the file.", e);
			JOptionPane.showMessageDialog(null, "Unable to open file.",
					Constants.ERROR_PREFIX, JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void checkSavedSearch() {
		if(StatusBean.isAnySavedSearchAvailable()) {
			loadSearchComboBox.setEnabled(true);
		} else {
			loadSearchComboBox.setEnabled(false);
		}
	}
	
	public static void start() {
		new GUIMain();
	}

	public void setInformation(String info) {
		currentStatusLabel.setText(info);
	}
	
}
