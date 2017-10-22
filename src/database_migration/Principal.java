package database_migration;

/*
 
 Réalisé par Anass Daoudi GI4 2016/2017 ENSA Marrakech
  
 */

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JFrame;

public class Principal {
	private TextField textField, textField_1, textField_2, textField_3, textField_4, textField_5, textField_6,
			textField_7;
	private Choice choice, choice_1;
	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal window = new Principal();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Principal() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Anass Daoudi GI4 2016/2017");
		frame.setBounds(100, 100, 550, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		Label label = new Label("Source");
		label.setAlignment(Label.CENTER);
		label.setBounds(48, 10, 110, 22);
		frame.getContentPane().add(label);

		Label label_1 = new Label("DB Name");
		label_1.setBounds(48, 106, 62, 22);
		frame.getContentPane().add(label_1);

		Label label_2 = new Label("User name");
		label_2.setFont(new Font("Dialog", Font.PLAIN, 11));
		label_2.setBounds(48, 148, 62, 22);
		frame.getContentPane().add(label_2);

		Label label_3 = new Label("Password");
		label_3.setBounds(48, 195, 62, 22);
		frame.getContentPane().add(label_3);

		Label label_4 = new Label("Port");
		label_4.setBounds(48, 236, 62, 22);
		frame.getContentPane().add(label_4);

		Label label_5 = new Label("Destination");
		label_5.setAlignment(Label.CENTER);
		label_5.setBounds(295, 10, 110, 22);
		frame.getContentPane().add(label_5);

		Label label_6 = new Label("DB Name");
		label_6.setBounds(295, 106, 62, 22);
		frame.getContentPane().add(label_6);

		Label label_7 = new Label("User name");
		label_7.setFont(new Font("Dialog", Font.PLAIN, 11));
		label_7.setBounds(295, 148, 62, 22);
		frame.getContentPane().add(label_7);

		Label label_8 = new Label("Password");
		label_8.setBounds(295, 195, 62, 22);
		frame.getContentPane().add(label_8);

		Label label_9 = new Label("Port");
		label_9.setBounds(295, 236, 62, 22);
		frame.getContentPane().add(label_9);

		Button button = new Button("Apply");
		button.setBackground(Color.GREEN);
		button.setFont(new Font("Dialog", Font.BOLD, 12));
		button.setBounds(417, 286, 70, 48);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean test1 = false, test2 = false, test3 = false, test4 = false, test5 = false, test6 = false;
				int port1 = 3306, port2 = 1521;

				if (textField.getText().isEmpty()) {
					textField.setText("Remplir svp");
				} else {
					test1 = true;
				}
				if (textField_1.getText().isEmpty()) {
					textField_1.setText("Remplir svp");
				} else {
					test2 = true;
				}
				try {
					port1 = Integer.parseInt(textField_3.getText());
					test3 = true;
				} catch (NumberFormatException ee) {
					textField_3.setText("Remplir avec entier");
				}

				if (textField_4.getText().isEmpty()) {
					textField_4.setText("Remplir svp");
				} else {
					test4 = true;
				}
				if (textField_5.getText().isEmpty()) {
					textField_5.setText("Remplir svp");
				} else {
					test5 = true;
				}
				try {
					port2 = Integer.parseInt(textField_7.getText());
					test6 = true;
				} catch (NumberFormatException ee) {
					textField_7.setText("Remplir avec entier");
				}
				if (test1 && test2 && test3 && test4 && test5 && test6) {
					ConnexionDB src = new ConnexionDB(choice.getSelectedItem(), textField.getText(),
							textField_1.getText(), textField_2.getText(), port1);
					ConnexionDB dest = new ConnexionDB(choice.getSelectedItem(), textField_4.getText(),
							textField_5.getText(), textField_6.getText(), port2);

					try {
						DatabaseMetaData dbmd = src.getConnection().getMetaData();
						ResultSet rs1 = dbmd.getTables(null, null, "%", null);

						while (rs1.next()) {
							String tableName = rs1.getString("TABLE_NAME");
							ResultSet rs2 = src.getStatement().executeQuery("select * from " + tableName);
							ResultSetMetaData rsmd = rs2.getMetaData();
							int columnsNumber = rsmd.getColumnCount();
							String columnsDescription = "";

							for (int i = 1; i <= columnsNumber; i++) {
								String type = null;

								if (rsmd.getColumnType(i) == 4) {
									type = "int";
								} else if (rsmd.getColumnType(i) == 12) {
									type = "varchar";
								}
								columnsDescription += rsmd.getColumnName(i) + " " + type + " ("
										+ rsmd.getColumnDisplaySize(i) + ")";
								if (i < columnsNumber) {
									columnsDescription += ",";
								}
							}
							DAOImpl o = new DAOImpl();
							o.createTable(dest, tableName, columnsDescription);

							while (rs2.next()) {
								String recordDescription = "";
								for (int i = 1; i <= columnsNumber; i++) {
									if (rsmd.getColumnType(i) == 4) {
										recordDescription += rs2.getInt(i);
									} else if (rsmd.getColumnType(i) == 12) {
										recordDescription += "'" + rs2.getString(i) + "'";
									}
									if (i < columnsNumber) {
										recordDescription += ",";
									}
								}
								o.insertRecord(dest, tableName, recordDescription);
							}
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}

					src.closeConnection();
					dest.closeConnection();
				}
			}
		});
		frame.getContentPane().add(button);

		textField = new TextField();
		textField.setBounds(116, 106, 130, 22);
		frame.getContentPane().add(textField);

		textField_1 = new TextField();
		textField_1.setBounds(116, 148, 130, 22);
		frame.getContentPane().add(textField_1);

		textField_2 = new TextField();
		textField_2.setEchoChar('*');
		textField_2.setBounds(116, 195, 130, 22);
		frame.getContentPane().add(textField_2);

		textField_3 = new TextField("3306");

		textField_3.setBounds(116, 236, 130, 22);
		frame.getContentPane().add(textField_3);

		textField_4 = new TextField();
		textField_4.setBounds(363, 106, 130, 22);
		frame.getContentPane().add(textField_4);

		textField_5 = new TextField();
		textField_5.setBounds(363, 148, 130, 22);
		frame.getContentPane().add(textField_5);

		textField_6 = new TextField();
		textField_6.setBounds(363, 195, 130, 22);
		frame.getContentPane().add(textField_6);

		textField_7 = new TextField("3306");
		textField_7.setBounds(363, 236, 130, 22);
		frame.getContentPane().add(textField_7);

		choice = new Choice();
		choice.add("mysql");
		choice.add("oracle");
		choice.setBounds(48, 38, 198, 20);
		choice.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (choice.getSelectedItem().equals("mysql")) {
					textField_3.setText("3306");
				} else if (choice.getSelectedItem().equals("oracle")) {
					textField_3.setText("1521");
				}
			}
		});
		frame.getContentPane().add(choice);

		choice_1 = new Choice();
		choice_1.setBounds(295, 38, 198, 20);
		choice_1.add("mysql");
		choice_1.add("oracle");
		choice_1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (choice_1.getSelectedItem().equals("mysql")) {
					textField_7.setText("3306");
				} else if (choice_1.getSelectedItem().equals("oracle")) {
					textField_7.setText("1521");
				}
			}
		});
		frame.getContentPane().add(choice_1);

		frame.setLocationRelativeTo(null);
	}
}
