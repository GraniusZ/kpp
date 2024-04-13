import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Zavd1_2 extends JFrame {
    private JTextField inputField;
    private JTextArea outputArea;

    public Zavd1_2() {
        super("Type Description");
        createGUI();
    }

    private void createGUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(200, 24));
        inputField.setToolTipText("Enter class name");

        JButton descriptionButton = new JButton("Description");
        descriptionButton.addActionListener(e -> showTypeDescription());

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            inputField.setText("");
            outputArea.setText("");
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(descriptionButton);
        buttonPanel.add(clearButton);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        setLayout(new BorderLayout(10, 10));
        add(inputField, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void showTypeDescription() {
        String typeName = inputField.getText().trim();
        if (!typeName.isEmpty()) {
            String description = getTypeDescription(typeName);
            outputArea.setText(description);
        } else {
            outputArea.setText("Please enter a class name.");
        }
    }

    private String getTypeDescription(String typeName) {
        try {
            Class<?> clazz = Class.forName(typeName);
            return getTypeDescription(clazz);
        } catch (ClassNotFoundException e) {
            return "Class not found: " + typeName;
        }
    }

    private String getTypeDescription(Class<?> clazz) {
        StringBuilder description = new StringBuilder();
        description.append("Package: ").append(clazz.getPackage()).append("\n");
        description.append("Modifiers: ").append(Modifier.toString(clazz.getModifiers())).append("\n");
        description.append("Class: ").append(clazz.getSimpleName()).append("\n");
        description.append(getSuperclassDescription(clazz));
        description.append(getInterfacesDescription(clazz));
        description.append(getFieldsDescription(clazz));
        description.append(getConstructorsDescription(clazz));
        description.append(getMethodsDescription(clazz));
        return description.toString();
    }

    private String getSuperclassDescription(Class<?> clazz) {
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            return "Superclass: " + superclass.getSimpleName() + "\n";
        }
        return "";
    }

    private String getInterfacesDescription(Class<?> clazz) {
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) {
            StringBuilder description = new StringBuilder("Implemented interfaces: ");
            for (Class<?> anInterface : interfaces) {
                description.append(anInterface.getSimpleName()).append(", ");
            }
            return description.substring(0, description.length() - 2) + "\n"; // Remove trailing comma and space
        }
        return "";
    }

    private String getFieldsDescription(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder description = new StringBuilder();
        if (fields.length > 0) {
            description.append("Fields:\n");
            for (Field field : fields) {
                description.append("\t").append(Modifier.toString(field.getModifiers()))
                        .append(" ").append(field.getType().getSimpleName())
                        .append(" ").append(field.getName()).append("\n");
            }
        }
        return description.toString();
    }

    private String getConstructorsDescription(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        StringBuilder description = new StringBuilder();
        if (constructors.length > 0) {
            description.append("Constructors:\n");
            for (Constructor<?> constructor : constructors) {
                description.append("\t").append(Modifier.toString(constructor.getModifiers()))
                        .append(" ").append(constructor.getName()).append(getParametersDescription(constructor.getParameterTypes()));
            }
        }
        return description.toString();
    }

    private String getMethodsDescription(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        StringBuilder description = new StringBuilder();
        if (methods.length > 0) {
            description.append("Methods:\n");
            for (Method method : methods) {
                description.append("\t").append(Modifier.toString(method.getModifiers()))
                        .append(" ").append(method.getReturnType().getSimpleName())
                        .append(" ").append(method.getName()).append(getParametersDescription(method.getParameterTypes()));
            }
        }
        return description.toString();
    }

    private String getParametersDescription(Class<?>[] parameterTypes) {
        StringBuilder parameters = new StringBuilder("(");
        for (int i = 0; i < parameterTypes.length; i++) {
            parameters.append(parameterTypes[i].getSimpleName());
            if (i < parameterTypes.length - 1) {
                parameters.append(", ");
            }
        }
        parameters.append(")\n");
        return parameters.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Zavd1_2().setVisible(true));
    }
}
