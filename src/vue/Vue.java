package vue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import controler.Controler;
import data.Tireur;
import observer.Observer;
import vue.tableau.MyTableModel;

public class Vue extends JFrame implements Observer
{
	private Controler controler;
	private JPanel mainPan = new JPanel(new BorderLayout());
	private MyTableModel tableModel = new MyTableModel();
	private JTable table = new JTable(tableModel);
	private TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this.table.getModel());
	private DefaultComboBoxModel<Object> comboModelPeloton = new DefaultComboBoxModel<Object>(Tireur.getPelotonCombo()), 
										 comboModelColonnes = new DefaultComboBoxModel<Object>(), 
										 comboModelCategorie = new DefaultComboBoxModel<Object>(), 
										 comboModelDistances = new DefaultComboBoxModel<Object>();
	private JComboBox<Object> filterPelotonCombo = new JComboBox<>(this.comboModelPeloton),
							  supprimerColonneCombo = new JComboBox<>(this.comboModelColonnes),
							  categorieCombo = new JComboBox<>(this.comboModelCategorie), 
							  distancesCombo = new JComboBox<>(this.comboModelDistances);
	
	public Vue(Controler controler){
		this.controler = controler;
	    this.setTitle("Automatic Archery");
	    this.setSize(1000, 600);
	    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    this.init();
	    this.initToolbar();
	    this.setContentPane(this.mainPan);
	}
	
	private void initToolbar(){
		JMenuBar menuBar = new JMenuBar();
		
		// ----- "Fichier": création, sauvegarde et chargement des fichiers
		
		JMenu fichier = new JMenu("Fichier");
		fichier.setMnemonic('f');
		menuBar.add(fichier);
		
		// nouveau fichier
		JMenuItem nouveau = new JMenuItem("Nouveau");
		nouveau.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		nouveau.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				if(controler.nouveau() == JOptionPane.OK_OPTION)
					controler.supprimerTireurs();
			}
		});
		fichier.add(nouveau);
		
		// sauvegarder le projet en cours
		JMenuItem sauvegarder = new JMenuItem("Enregistrer");
		sauvegarder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		sauvegarder.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				controler.sauvegarder(false);
			}
		});
		fichier.add(sauvegarder);
		
		// sauvegarder le projet en cours sous un nouveau nom
		JMenuItem sauvegarderSous = new JMenuItem("Enregistrer sous...");
		sauvegarderSous.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
		sauvegarderSous.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				controler.sauvegarder(true);
			}
		});
		fichier.add(sauvegarderSous);
		
		// charger un nouveau fichier
		JMenuItem ouvrir = new JMenuItem("Ouvrir");
		ouvrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		ouvrir.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				controler.ouvrir();
			}
		});
		fichier.add(ouvrir);
		
		// supprimer un fichier
		JMenuItem supprimer = new JMenuItem("Supprimer");
		supprimer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK));
		supprimer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				controler.supprimer();
			}
		});
		fichier.add(supprimer);
		
		
		// --- Exporter
		fichier.addSeparator();
		
		JMenu exporter = new JMenu("Exporter");
		fichier.add(exporter);
		
		JMenuItem planTir = new JMenuItem("Plan de tir");
		planTir.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				controler.getPlanCible();
			}
		});
		exporter.add(planTir);
		
		JMenuItem planTireurs = new JMenuItem("Plan des tireurs");
		planTireurs.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				controler.getPlanTireurs();
			}
		});
		exporter.add(planTireurs);
		
		JMenuItem feuillesMarque = new JMenuItem("Feuilles de marque");
		feuillesMarque.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				// TODO
				JOptionPane.showMessageDialog(null, "Pas encore implémenté", "Work in progress", JOptionPane.WARNING_MESSAGE);
			}
		});
		exporter.add(feuillesMarque);
		
		JMenuItem classement = new JMenuItem("Classement");
		classement.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				controler.getClassement();
			}
		});
		exporter.add(classement);

		
		// ----- "Outils": Modifitcation des paramètres de la compétition
		
		JMenu outils = new JMenu("Outils");
		outils.setMnemonic('o');
		menuBar.add(outils);
		
		// Modifier le nombre de tireurs par cible
		JMenu ABC_ABCD = new JMenu("ABC/ABCD");
		JRadioButtonMenuItem ABC = new JRadioButtonMenuItem("ABC");
		JRadioButtonMenuItem ABCD = new JRadioButtonMenuItem("ABCD");
		ABCD.setSelected(true);
		ButtonGroup ABC_ABCD_group = new ButtonGroup();
		ABC_ABCD_group.add(ABC);
		ABC_ABCD_group.add(ABCD);
		ActionListener ABC_ABCD_listener = new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				controler.setABCD(((JRadioButtonMenuItem)evt.getSource()).getText());
			}
		};
		ABC.addActionListener(ABC_ABCD_listener);
		ABCD.addActionListener(ABC_ABCD_listener);
		ABC_ABCD.add(ABC);
		ABC_ABCD.add(ABCD);
		outils.add(ABC_ABCD);
		
		
		// --- Actions sur les competitions
		outils.addSeparator();
		
		// Sélectionner un type de competition
		JMenuItem selectCompetition = new JMenuItem("Selectionner un type de competition");
		selectCompetition.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				controler.selectCompetition();
			}
		});
		outils.add(selectCompetition);

		// Ajouter une competition
		JMenuItem ajouterCompetition = new JMenuItem("Créer un type de compétition");
		ajouterCompetition.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				controler.ajouterCompetition();
			}
		});
		outils.add(ajouterCompetition);

		// Supprimer une competition
		JMenuItem supprimerCompetition = new JMenuItem("Supprimer un type de compétition");
		supprimerCompetition.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				controler.supprimerCompetition();
			}
		});
		outils.add(supprimerCompetition);
		
		// --- Actions sur les catégories
		outils.addSeparator();
		
		// Ajouter une categorie
		JMenuItem ajouterCategorie = new JMenuItem("Ajouter une nouvelle catégorie");
		ajouterCategorie.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				controler.ajouterCategorie();
			}
		});
		outils.add(ajouterCategorie);

		// Supprimer une competition
		JMenuItem supprimerCategorie = new JMenuItem("Supprimer une catégorie");
		supprimerCategorie.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				controler.supprimerCategorie();
			}
		});
		outils.add(supprimerCategorie);
		

		// --- À propos
		JMenu aPropos = new JMenu("À propos");
		menuBar.add(aPropos);
		JMenuItem aProposItem = new JMenuItem("?");
		aProposItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				JOptionPane.showMessageDialog(null, "Auteurs:\n- Chris ADAM, étudiant en bioinformatique"
						+ " \n- Frédéric ADAM, tireur et arbitre LFBTA \n\nCréé en 2019\n ", "AutomaticArchery", JOptionPane.PLAIN_MESSAGE);
			}
		});
		aPropos.add(aProposItem);
		
		
		this.setJMenuBar(menuBar);
	}
	
	private void init(){
		JPanel form = this.initForm();
		form.setPreferredSize(new Dimension(450, (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight())-600));
		JScrollPane formSP = new JScrollPane(form);
		formSP.setHorizontalScrollBar(null);
		
		JScrollPane tab = this.initTab();
		tab.setPreferredSize(new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()-450), 
				(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight())));

		this.mainPan.add(formSP, BorderLayout.EAST);
		this.mainPan.add(tab, BorderLayout.CENTER);
	}
	
	private JScrollPane initTab(){
		JScrollPane jsp = new JScrollPane(table);

		// Gère la suppression d'un tireur et l'édition des scores
		this.table.setAutoCreateRowSorter(true);
		this.table.setRowSorter(sorter);
		this.table.addKeyListener(new KeyListener(){
		    public void keyReleased(KeyEvent evt){
		    	if(evt.getKeyCode() == KeyEvent.VK_DELETE)
		    		controler.supprimerTireur(tableModel.getRowID(table.getSelectedRow()));
		    	else if(tableModel.getRowCount() >= table.getSelectedRow()+1 && evt.getKeyCode() == KeyEvent.VK_ENTER
		    			&& (tableModel.getColumnName(table.getSelectedColumn()) == Tireur.title[13] || 
		    					tableModel.getColumnName(table.getSelectedColumn()) == Tireur.title[16]))
		    		table.changeSelection(table.getSelectedRow(), table.getSelectedColumn()-2, false, false);
			}
		    public void keyPressed(KeyEvent evt) {}
		    public void keyTyped(KeyEvent evt) {}
		});
		
		// Pour centrer le texte dans les cellules
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		this.table.setDefaultRenderer(Integer.class, centerRenderer);
		this.table.setDefaultRenderer(String.class, centerRenderer);
		
		this.table.getTableHeader().setReorderingAllowed(false);
		
		return jsp;
	}
	
	private JPanel initForm(){
		JPanel formulaire = new JPanel();
		
		// ---------------- Formulaire d'ajout d'un nouveau tireur

		String[] titresChamps = {"Nom", "Prénom", "Club", "Catégorie", "Matricule", "Peloton"};
		JButton annuler = new JButton("Annuler"), ajouter = new JButton("Ajouter");
		ArrayList<JComponent> champs = new ArrayList<JComponent>();
		Font font = new Font(null, Font.PLAIN, 20);
		JPanel formAddTireur = new JPanel(new GridLayout(titresChamps.length+1, 2));
		class FormulaireKeyListener implements KeyListener{
			public void keyTyped(KeyEvent evt) {}
			public void keyPressed(KeyEvent evt) {if(evt.getKeyCode() == KeyEvent.VK_ENTER) ajouter.doClick();}
			public void keyReleased(KeyEvent evt) {}
		}
		FormulaireKeyListener fkl = new FormulaireKeyListener();
		ajouter.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				if(controler.ajouterTireur(champs)){
					for(JComponent comp : champs) {
						if(comp instanceof JTextField)
							((JTextField)comp).setText("");
						else if(comp instanceof JComboBox)
							((JComboBox<?>)comp).setSelectedItem(0);
					}
					champs.get(0).requestFocus();
				}
			}
		});
		annuler.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				for(JComponent comp : champs) {
					if(comp instanceof JTextField)
						((JTextField)comp).setText("");
					else if(comp instanceof JComboBox)
						((JComboBox<?>)comp).setSelectedItem(0);						
				}
				champs.get(0).requestFocus();
			}
		});
		
		for(int i = 0 ; i < titresChamps.length ; i++){
			JLabel label = new JLabel(titresChamps[i]+" : ", JLabel.RIGHT);
			label.setFont(font);
			formAddTireur.add(label);
			JPanel panTextField = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			
			if(titresChamps[i] == "Catégorie"){
				JComboBox<Object> comboCat = new JComboBox<Object>(this.comboModelCategorie);
				champs.add(comboCat);
			}
			else if(titresChamps[i] == "Peloton")
				champs.add(new JFormattedTextField(NumberFormat.getIntegerInstance()));
			else
				champs.add(new JTextField());
			champs.get(i).addKeyListener(fkl);
			champs.get(i).setPreferredSize(new Dimension(200, 30));
			panTextField.add(champs.get(i), gbc);
			formAddTireur.add(panTextField);
		}
		JPanel annulerPan = new JPanel();
		annulerPan.add(annuler);
		formAddTireur.add(annulerPan);
		JPanel ajouterPan = new JPanel();
		ajouterPan.add(ajouter);
		formAddTireur.add(ajouterPan);
		ajouter.addKeyListener(fkl);
		formAddTireur.setBorder(BorderFactory.createTitledBorder("Ajouter un tireur"));
		formulaire.add(formAddTireur);
		
		// ------------ Options supplémentaires (ajouter une colonne custom)
		
		JPanel optionsPan = new JPanel(new GridLayout(6, 2));
		
		// Recherche rapide
		JTextField rechercheRapide = new JTextField();
		rechercheRapide.addKeyListener(new KeyListener(){
			public void keyTyped(KeyEvent evt) {}
			public void keyPressed(KeyEvent evt) {}
			public void keyReleased(KeyEvent evt) {
			    RowFilter<TableModel, Object> rf = null;
			    //If current expression doesn't parse, don't update.
			    try {
			        rf = RowFilter.regexFilter(rechercheRapide.getText());
			    } catch (PatternSyntaxException e) {}
			    sorter.setRowFilter(rf);
			   }
		});
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher(){
	        @Override
	        public boolean dispatchKeyEvent(KeyEvent evt) {
	            if (evt.getID() == KeyEvent.KEY_RELEASED && evt.getKeyCode() == KeyEvent.VK_F && evt.getModifiers() == KeyEvent.CTRL_MASK){
	            	rechercheRapide.requestFocus();
	            	rechercheRapide.selectAll();
	            }
	            return false;
	        }
		});
		JLabel label = new JLabel("Recherche : ", JLabel.RIGHT);
		label.setToolTipText("Recherche rapide");
		label.setFont(font);
		optionsPan.add(label);
		optionsPan.add(rechercheRapide);
		rechercheRapide.setPreferredSize(new Dimension(200, 30));
		
		// Filtrer un peloton
		filterPelotonCombo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				if(filterPelotonCombo.getSelectedItem() != null)
					controler.setPelotonFilter(Integer.valueOf(filterPelotonCombo.getSelectedItem().toString()));
			}
		});
		label = new JLabel("Filtrer peloton : ", JLabel.RIGHT);
		label.setToolTipText("Affiche le peloton séléctionné, 0 affiche tous les pelotons");
		label.setFont(font);
		optionsPan.add(label);
		optionsPan.add(filterPelotonCombo);
		filterPelotonCombo.setPreferredSize(new Dimension(200, 30));
		
		// Ajouter une colonne perso
		JTextField addCustomColTF = new JTextField();
		addCustomColTF.addKeyListener(new KeyListener(){
			public void keyTyped(KeyEvent evt) {}
			public void keyPressed(KeyEvent evt) {
				if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
					controler.addCol(addCustomColTF.getText());
					addCustomColTF.setText("");
				}
			}
			public void keyReleased(KeyEvent evt) {}
		});
		label = new JLabel("Nouvelle colonne : ", JLabel.RIGHT);
		label.setToolTipText("Crée une nouvelle colonne");
		label.setFont(font);
		optionsPan.add(label);
		optionsPan.add(addCustomColTF);
		addCustomColTF.setPreferredSize(new Dimension(200, 30));
		
		// Supprimer une colonne perso
		supprimerColonneCombo.addKeyListener(new KeyListener(){
			public void keyTyped(KeyEvent evt) {}
			public void keyPressed(KeyEvent evt) {
				if(supprimerColonneCombo.getSelectedItem() != null && (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_DELETE))
					controler.supprimerColonne(supprimerColonneCombo.getSelectedItem().toString());
			}
			public void keyReleased(KeyEvent evt) {}
		});
		label = new JLabel("Supprimer colonne : ", JLabel.RIGHT);
		label.setToolTipText("Supprime une des colonnes personnalisées");
		label.setFont(font);
		optionsPan.add(label);
		optionsPan.add(supprimerColonneCombo);
		supprimerColonneCombo.setPreferredSize(new Dimension(200, 30));
		
		// Assigner automatiquement les cibles
		JButton setPlanCible = new JButton("Assigner les cibles");
		setPlanCible.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				controler.setPlanCible();
			}
		});
		label = new JLabel("Assigner : ", JLabel.RIGHT);
		label.setToolTipText("Assigne automatiquement les cibles et numéros de blason aux tireurs.");
		label.setFont(font);
		optionsPan.add(label);
		optionsPan.add(setPlanCible);
		setPlanCible.setPreferredSize(new Dimension(200, 30));
		
		// Assigner automatiquement les distances et types de blason
		JButton setPlanBlasons = new JButton("Assigner les blasons");
		setPlanBlasons.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				controler.setPlanBlasons();
			}
		});
		label = new JLabel("Assigner : ", JLabel.RIGHT);
		label.setToolTipText("Assigne automatiquement les distances et types de blason aux tireurs en fonction de leur catégorie.");
		label.setFont(font);
		optionsPan.add(label);
		optionsPan.add(setPlanBlasons);
		setPlanBlasons.setPreferredSize(new Dimension(200, 30));
				
	    
		optionsPan.setBorder(BorderFactory.createTitledBorder("Options"));
		formulaire.add(optionsPan);
			
		return formulaire;
	}
	
	public void update(ArrayList<Tireur> listTireurs, String frameTitle) {
		// MAJ du titre de la fenêtre en relation avec la compétition en curs de traitement
		this.setTitle("AutomaticArchery" + frameTitle);
		
		// MAJ de la comboBox pour le filtrage des pelotons
		Object selectedObject = this.comboModelPeloton.getSelectedItem();
		this.comboModelPeloton.removeAllElements();
		for(int i = 0 ; i < Tireur.getPelotonCombo().length ; i++)
			this.comboModelPeloton.addElement(Tireur.getPelotonCombo()[i]);
		try {this.filterPelotonCombo.setSelectedIndex(this.comboModelPeloton.getIndexOf(selectedObject));}
		catch(IllegalArgumentException e) {this.filterPelotonCombo.setSelectedIndex(0);}

		// MAJ de la comboBox pour supprimer les colonnes perso
		selectedObject = this.comboModelColonnes.getSelectedItem();
		this.comboModelColonnes.removeAllElements();
		for(int i = 0 ; i < Tireur.getCustomTitle().size() ; i++)
			this.comboModelColonnes.addElement(Tireur.getCustomTitle().get(i));
		try {this.supprimerColonneCombo.setSelectedIndex(this.comboModelColonnes.getIndexOf(selectedObject));}
		catch(IllegalArgumentException e) {this.supprimerColonneCombo.setSelectedIndex(0);}
		
		// MAJ de la comboBox pour choisir une catégorie
		this.comboModelCategorie.removeAllElements();
		for(int i = 0 ; i < Tireur.getCatCombo().length ; i++)
			this.comboModelCategorie.addElement(Tireur.getCatCombo()[i]);
		
		// MAJ de la comboBox pour choisir une distance
		this.comboModelDistances.removeAllElements();
		for(int i = 0 ; i < Tireur.getDistCombo().length ; i++)
			this.comboModelDistances.addElement(Tireur.getDistCombo()[i]);
		
		
		// MAj du tableau
		this.tableModel.update(listTireurs);
	    this.table.getColumn("Statut").setCellEditor(new DefaultCellEditor(new JComboBox<>(Tireur.listStatuts)));
	    this.table.getColumn("Catégorie").setCellEditor(new DefaultCellEditor(this.categorieCombo));
	    this.table.getColumn("Type de blason").setCellEditor(new DefaultCellEditor(new JComboBox<>(Tireur.getListTypeBlason().toArray())));
	    this.table.getColumn("Distance").setCellEditor(new DefaultCellEditor(this.distancesCombo));
	}
}
