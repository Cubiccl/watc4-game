package net.watc4.editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.watc4.game.entity.Entity;
import net.watc4.game.entity.EntityBattery;
import net.watc4.game.entity.EntityRegistry;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;

public class EntityValues extends JDialog
{

	private final JPanel contentPanel = new JPanel();
	private static JLabel[] fieldsName;
	private static JTextField[] fields;
	private static Entity en;
	private static String[] definitions;

	public static boolean checkFields()
	{
		for (int i = 2; i < fields.length; i++)
		{
			if (definitions[1 + i * 2].equals("unsigned int"))
			{
				int f = 0;
				try
				{
					f = Integer.parseInt(fields[i].getText());
				} catch (Exception e)
				{
					return false;
				}
				if (f < 0) return false;
			} else if (definitions[1 + i * 2].equals("int"))
			{
				int f = 0;
				try
				{
					f = Integer.parseInt(fields[i].getText());
				} catch (Exception e)
				{
					return false;
				}
			} else if (definitions[1 + i * 2].equals("unsigned float"))
			{
				float f = 0;
				try
				{
					f = Float.parseFloat(fields[i].getText());
				} catch (Exception e)
				{
					return false;
				}
				if (f < 0) return false;
			} else if (definitions[1 + i * 2].equals("float"))
			{
				float f = 0;
				try
				{
					f = Float.parseFloat(fields[i].getText());
				} catch (Exception e)
				{
					return false;
				}
			}
		}
		return true;
	}

	/** Create the dialog. */
	public EntityValues(TileLabel tl)
	{
		this.en = tl.getEn();
		EntityRegistry.defineEntities();
		this.definitions = EntityRegistry.getDefinitions().get(en.getClass());
		setBounds(100, 100, 400, 75 * (en.getClass().getConstructors()[0].getParameterCount() - 3));
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
								if (definitions[1 + i * 2].equals("unsigned int") || definitions[1 + i * 2].equals("int")) values[i + 1] = Integer
										.parseInt(fields[i].getText());
								else if (definitions[1 + i * 2].equals("unsigned float") || definitions[1 + i * 2].equals("float")) values[i + 1] = Float
										.parseFloat(fields[i].getText());
								else values[i + 1] = fields[i].getText();
							}
							try
							{
								EntityValues.this.en = (Entity) en.getClass().getConstructors()[0].newInstance(values);
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
		fields = new JTextField[en.getClass().getConstructors()[0].getParameterCount() - 1];
		fieldsName = new JLabel[fields.length];

		for (int i = 0; i < fields.length; i++)
		{
			fields[i] = new JTextField(String.valueOf(tl.getEntityValues()[i + 1]));
			if (i >= 2)
			{
				fieldsName[i] = new JLabel(definitions[i * 2] + " (" + definitions[1 + i * 2] + ") : ");
				fieldsName[i].setBounds(30, -73 + 40 * i, 150, 20);
				fields[i].setBounds(250, -70 + 40 * i, 100, 20);
				contentPanel.add(fields[i]);
				contentPanel.add(fieldsName[i]);
			}
		}
	}

}
