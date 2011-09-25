package com.googlecode.positionalir.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;

import com.googlecode.positionalir.engine.Indexer;
import com.googlecode.positionalir.engine.Parser;
import com.googlecode.positionalir.engine.Tokenizer;
import com.googlecode.positionalir.model.Document;
import com.googlecode.positionalir.model.Index;
import com.googlecode.positionalir.search.SearchEngine;
import com.googlecode.positionalir.serializer.IndexSerializer;

public class MainGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3702331582394389523L;
	
	private Index index = new Index();
	
	private JPanel contentPane;
	private JTextField textFieldQueryInput;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainGUI frame = new MainGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainGUI() {
		setTitle("Positional IR");
		setMaximumSize(new Dimension(900, 810));
		setMinimumSize(new Dimension(300, 270));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 526, 403);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JButton btnIndexFiles = new JButton("Index new Files ");
		btnIndexFiles.addMouseListener(new MouseAdapter() {
			private File lastDir = new File(".");
			@Override
			public void mouseReleased(MouseEvent arg0) {
				final JFileChooser jFileChooser = new JFileChooser(lastDir);
				jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jFileChooser.setFileFilter(new FileNameExtensionFilter("SGML file.", "sgm"));
				jFileChooser.setMultiSelectionEnabled(true);
				final int showOpenDialog = jFileChooser.showOpenDialog(contentPane);
				if(showOpenDialog == JFileChooser.APPROVE_OPTION) {
					final File[] selectedFiles = jFileChooser.getSelectedFiles();
					final Indexer indexer = new Indexer(index);
					for (File file : selectedFiles) {
						if(file.isDirectory())
							continue;
						indexer.index(file.getAbsolutePath());
						lastDir = file.getParentFile();
					}
				}
			}
		});
		GridBagConstraints gbc_btnIndexFiles = new GridBagConstraints();
		gbc_btnIndexFiles.gridwidth = 3;
		gbc_btnIndexFiles.insets = new Insets(0, 0, 5, 0);
		gbc_btnIndexFiles.gridx = 0;
		gbc_btnIndexFiles.gridy = 0;
		contentPane.add(btnIndexFiles, gbc_btnIndexFiles);
		
		JLabel lblQuery = new JLabel("Query:");
		GridBagConstraints gbc_lblQuery = new GridBagConstraints();
		gbc_lblQuery.weighty = 1.0;
		gbc_lblQuery.anchor = GridBagConstraints.EAST;
		gbc_lblQuery.insets = new Insets(0, 0, 5, 5);
		gbc_lblQuery.gridx = 0;
		gbc_lblQuery.gridy = 1;
		contentPane.add(lblQuery, gbc_lblQuery);
		
		textFieldQueryInput = new JTextField();
		
		textFieldQueryInput.setText("Scrivi qui la query");
		GridBagConstraints gbc_textFieldQueryInput = new GridBagConstraints();
		gbc_textFieldQueryInput.weightx = 30.0;
		gbc_textFieldQueryInput.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldQueryInput.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldQueryInput.gridx = 1;
		gbc_textFieldQueryInput.gridy = 1;
		contentPane.add(textFieldQueryInput, gbc_textFieldQueryInput);
		textFieldQueryInput.setColumns(10);
		
		final JButton btnRun = new JButton("Run");
		GridBagConstraints gbc_btnRun = new GridBagConstraints();
		gbc_btnRun.anchor = GridBagConstraints.EAST;
		gbc_btnRun.insets = new Insets(0, 0, 5, 0);
		gbc_btnRun.gridx = 2;
		gbc_btnRun.gridy = 1;
		contentPane.add(btnRun, gbc_btnRun);
		
		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.weighty = 30.0;
		gbc_tabbedPane.anchor = GridBagConstraints.NORTH;
		gbc_tabbedPane.gridheight = 2;
		gbc_tabbedPane.gridwidth = 3;
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 2;
		contentPane.add(tabbedPane, gbc_tabbedPane);
		
		final JTextArea txtrResult = new JTextArea();
		txtrResult.setEditable(false);
		txtrResult.setText("Result");
		DefaultCaret caret = (DefaultCaret) txtrResult.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		
		JScrollPane scrollPane = new JScrollPane(txtrResult);
		tabbedPane.addTab("Result", null, scrollPane, null);

		btnRun.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				doSearch(tabbedPane, txtrResult);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				this.mouseReleased(e);
			}
			
		});
		textFieldQueryInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				if(ke.getKeyCode() == KeyEvent.VK_ENTER) {
					doSearch(tabbedPane, txtrResult);
				}
			}
		});
		
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.gridwidth = 3;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 4;
		contentPane.add(panel, gbc_panel);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		
		JButton btnLoadIndex = new JButton("Load index");
		btnLoadIndex.addMouseListener(new MouseAdapter() {
			private File lastDir = new File(".");
			@Override
			public void mouseReleased(MouseEvent e) {
				final JFileChooser jFileChooser = new JFileChooser(lastDir);
				jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jFileChooser.setFileFilter(new FileNameExtensionFilter("Index file.", "index"));
				final int showOpenDialog = jFileChooser.showOpenDialog(contentPane);
				if(showOpenDialog == JFileChooser.APPROVE_OPTION) {
					final File selectedFile = jFileChooser.getSelectedFile();
					final IndexSerializer indexSerializer = new IndexSerializer(selectedFile.getAbsolutePath());
					index = indexSerializer.loadIndex();
				}
			}
		});
		panel_1.add(btnLoadIndex);
		btnLoadIndex.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		
		JButton btnSaveIndex = new JButton("Save Index");
		btnSaveIndex.addMouseListener(new MouseAdapter() {
			private File lastDir = new File(".");
			@Override
			public void mouseReleased(MouseEvent e) {
				final JFileChooser jFileChooser = new JFileChooser(lastDir);
				jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jFileChooser.setFileFilter(new FileNameExtensionFilter("Index file.", "index"));
				final int showOpenDialog = jFileChooser.showSaveDialog(contentPane);
				if(showOpenDialog == JFileChooser.APPROVE_OPTION) {
					final File selectedFile = jFileChooser.getSelectedFile();
					final IndexSerializer indexSerializer = new IndexSerializer(selectedFile.getAbsolutePath());
					indexSerializer.saveIndex(index);
				}
			}
		});
		panel_2.add(btnSaveIndex);
		btnSaveIndex.setAlignmentX(Component.CENTER_ALIGNMENT);
	}

	protected void doSearch(final JTabbedPane tabbedPane, final JTextArea txtrResult) {
		final SearchEngine searchEngine = new SearchEngine(index);
		final String query = textFieldQueryInput.getText();
		final String[] docIDs = searchEngine.searchQuery(query);
		final StringBuilder result = new StringBuilder();
		final Map<String, Document> docID2DocumentCache = buildDocID2DocumentCache(new HashSet<String>(Arrays.asList(docIDs)));
		final Tokenizer tokenizer = new Tokenizer();
		for (final String docID : docIDs) {
			result.append(docID);
			final Document document = docID2DocumentCache.get(docID);
			final String all = tokenizer.removePunctuation(document.getAll());
			final int indexOf = all.indexOf(tokenizer.removePunctuation(query));
			result.append(" - {").append(slimString(tokenizer.removePunctuation(document.getTitle()), 80)).append("}");
			if(indexOf != -1) {
				result.append("\n\t");
				result.append(all.substring(Math.max(0, indexOf - 10), Math.min(all.length(), indexOf + query.length() + 50)));
			}
			result.append("\n\r");
		}
		tabbedPane.setTitleAt(0, "Result (" + docIDs.length + ")");
		txtrResult.setText(result.toString());
	}

	protected String slimString(final String string, int maxLenght) {
		if(maxLenght < string.length()) 
			return string.substring(0, maxLenght - 3) + "...";
		return string;
	}

	private Map<String, Document> buildDocID2DocumentCache(Set<String> docIDs) {
		final Map<String, Document> result = new HashMap<String, Document>(docIDs.size());
		Set<String> fileToParse = new HashSet<String>(20);
		for (String docID : docIDs) {
			fileToParse.add(index.getFilePath(Integer.parseInt(docID)));
		}
		for (String file : fileToParse) {
			try {
				List<Document> parse = new Parser().parse(new BufferedReader(new FileReader(new File(file))));
				for (Document document : parse) {
					if(docIDs.contains(document.getNewid())) {
						result.put(document.getNewid(), document);
					}
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

}
