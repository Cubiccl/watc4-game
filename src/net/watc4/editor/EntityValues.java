package net.watc4.editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.watc4.game.entity.Entity;
import net.watc4.game.entity.EntityRegistry;

@SuppressWarnings("serial")
public class EntityValues extends JDialog
{

	private static String[] definitions;
	private static Entity en;
	private static int enId;
	private static JTextField[] fields;
	private static JComboBox[] cutsceneFields, mapFields;
	private static JLabel[] fieldsName;
	private static TileLabel tl;

	public static String getText(int i)
	{
		switch (definitions[i * 2])
		{
			case "Cutscene Name":
				return ((String) cutsceneFields[i].getSelectedItem());
			case "Map Name":
				return ((String) mapFields[i].getSelectedItem());
			default:
				return fields[i].getText();
		}
	}

	public static boolean checkFields()
	{
		for (int i = 2; i < fields.length; i++)
		{
			if (definitions[i * 2].equals("UUID"))
			{
				int f = 0;
				try
				{
					f = Integer.parseInt(getText(i));
				} catch (Exception e)
				{
					System.out.println(1);
					return false;
				}
				if (f < enId * 100 || f >= (enId + 1) * 100) return false;
			}
			if (definitions[1 + i * 2].equals("unsigned int"))
			{
				int f = 0;
				try
				{
					f = Integer.parseInt(getText(i));
				} catch (Exception e)
				{
					System.out.println(2);
					return false;
				}
				if (f < 0) return false;
			} else if (definitions[1 + i * 2].equals("int"))
			{
				try
				{
					Integer.parseInt(getText(i));
				} catch (Exception e)
				{
					System.out.println(3);
					return false;
				}
			} else if (definitions[1 + i * 2].equals("unsigned float"))
			{
				float f = 0;
				try
				{
					f = Float.parseFloat(getText(i));
				} catch (Exception e)
				{
					System.out.println(4);
					return false;
				}
				if (f < 0) return false;
			} else if (definitions[1 + i * 2].equals("float"))
			{
				try
				{
					Float.parseFloat(getText(i));
				} catch (Exception e)
				{
					System.out.println(5);
					return false;
				}
			}
		}
		return true;
	}

	private final JPanel contentPanel = new JPanel();

	/** Create the dialog. */
	public EntityValues(TileLabel tile)
	{
		tl = tile;
		enId = tl.getEnId();
		en = tl.getEn();
		setModal(true);
		definitions = EntityRegistry.getDefinitions().get(en.getClass());
		setBounds(100, 100, 400, 40 * (en.getClass().getConstructors()[1].getParameterCount() - 1));
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent e)
					{
						if (checkFields())
						{
							Object[] values = new Object[fields.length + 1];
							values[0] = null;
							values[1] = en.getX();
							values[2] = en.getY();
							for (int i = 0; i < values.length - 1; i++)
							{
								if (definitions[1 + i * 2].startsWith("unsigned int") || definitions[1 + i * 2].equals("int")) values[i + 1] = Integer
										.parseInt(getText(i));
								else if (definitions[1 + i * 2].startsWith("unsigned float") || definitions[1 + i * 2].equals("float")) values[i + 1] = Float
										.parseFloat(getText(i));
								else values[i + 1] = getText(i);
							}
							try
							{
								EntityValues.en = (Entity) en.getClass().getConstructors()[1].newInstance(values);
								tl.setEntityValues(values);
								tl.getEntityValues()[0] = tl.getEnId();
								EntityValues.this.dispose();
							} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e1)
							{
								e1.printStackTrace();
							}
						} else JOptionPane.showMessageDialog(null, "Veuillez remplir les champs correctement.", null, JOptionPane.ERROR_MESSAGE);
					}

				});
				{
					JButton btnSupprimer = new JButton("Supprimer");
					btnSupprimer.addMouseListener(new MouseAdapter()
					{
						@Override
						public void mouseClicked(MouseEvent arg0)
						{
							EntityValues.this.dispose();
							tl.setEn(null);
							tl.setEntityValues(null);
							tl.removeAll();
							tl.updateUI();
						}
					});
					buttonPane.add(btnSupprimer);
				}
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Annuler");
				cancelButton.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent arg0)
					{
						EntityValues.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		contentPanel.setLayout(null);
		fields = new JTextField[definitions.length / 2];
		cutsceneFields = new JComboBox[definitions.length / 2];
		mapFields = new JComboBox[definitions.length / 2];
		fieldsName = new JLabel[fields.length];

		int currentY = 10;
		for (int i = 2; i < fields.length; i++)
		{
			switch (definitions[i * 2])
			{
				case "Cutscene Name":
				{
					cutsceneFields[i] = new JComboBox<String>(EventLabelCutscene.getCutsceneList());
					cutsceneFields[i].setBounds(250, currentY + 3, 100, 20);
					contentPanel.add(cutsceneFields[i]);
					if (!tl.getEntityValues()[6].equals("null"))
					{
						cutsceneFields[i].setSelectedItem(tl.getEntityValues()[6]);
					}
				}
				case "Map Name":
				{
					mapFields[i] = new JComboBox<String>(EventLabelCutscene.getMapList());
					mapFields[i].setBounds(250, currentY + 3, 100, 20);
					contentPanel.add(mapFields[i]);
					if (!tl.getEntityValues()[6].equals("null"))
					{
						mapFields[i].setSelectedItem(tl.getEntityValues()[6]);
					}
				}
				default:
				{
					fields[i] = new JTextField(String.valueOf(tl.getEntityValues()[i + 1]));
					fields[i].setBounds(250, currentY + 3, 100, 20);
					contentPanel.add(fields[i]);
				}
			}
			fieldsName[i] = new JLabel(definitions[i * 2] + " (" + definitions[1 + i * 2] + ") : ");
			fieldsName[i].setBounds(30, currentY, 200, 20);
			contentPanel.add(fieldsName[i]);
			currentY += 40;
		}
	}

}
