package hr.tvz.boggle.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DocumentationUtils {

    /**
     * Generates HTML documentation for all Java classes found in the specified directory.
     * This method uses reflection to extract information about classes, their fields,
     * constructors, and methods, then writes it to an HTML file.
     */
    public static void generateDocumentation() {

        StringBuilder documentationGenerator = new StringBuilder();

        String path = "C:\\Users\\lkranjcec\\IdeaProjects\\BoggleApplication\\src\\main\\java";

        try {
            List<Path> classNameList = Files.walk(Paths.get(path))
                    .filter(p -> p.getFileName().toString().endsWith(".java"))
                    .filter(p -> !p.getFileName().toString().equals("module-info.java"))
                    .toList();

            // Iterate over each Java file found
            for (Path classPath : classNameList) {
                // Find the index where the package name (starting with "hr") begins
                int indexOfHr = classPath.toString().indexOf("hr");
                // Extract the fully qualified class name from the file path
                String fqcn = classPath.toString().substring(indexOfHr);

                // Replace file separators with dots and remove the ".java" extension
                fqcn = fqcn.replace('\\', '.');
                fqcn = fqcn.substring(0, fqcn.length() - 5);

                // Load the class using its fully qualified name
                Class<?> documentationClass = Class.forName(fqcn);
                // Get the class modifiers (e.g., public, abstract, etc.)
                String classModifiers = Modifier.toString(documentationClass.getModifiers());
                // Append the class header (including its modifiers and fully qualified name) to the HTML content
                documentationGenerator.append("<h2>")
                        .append(classModifiers)
                        .append(" ")
                        .append(fqcn)
                        .append("</h2>\n");

                // Retrieve all declared fields in the class
                Field[] classVariables = documentationClass.getDeclaredFields();
                if (classVariables.length > 0) {
                    documentationGenerator.append("<h3>Fields:</h3>\n");
                    // For each field, document its modifiers, type, and name
                    for (Field field : classVariables) {
                        String modifiers = Modifier.toString(field.getModifiers());
                        documentationGenerator.append("<p>")
                                .append(modifiers)
                                .append(" ")
                                .append(field.getType().getName())
                                .append(" ")
                                .append(field.getName())
                                .append("</p>\n");
                    }
                }

                Constructor[] classConstructors = documentationClass.getDeclaredConstructors();
                if (classConstructors.length > 0) {
                    documentationGenerator.append("<h3>Constructors:</h3>\n");
                    for (Constructor constructor : classConstructors) {
                        String modifiers = Modifier.toString(constructor.getModifiers());
                        documentationGenerator.append("<p>")
                                .append(modifiers)
                                .append(" ")
                                .append(constructor.getName())
                                .append("(");

                        Parameter[] parameters = constructor.getParameters();
                        for (int i = 0; i < parameters.length; i++) {
                            if (i > 0) {
                                documentationGenerator.append(", ");
                            }
                            documentationGenerator.append(parameters[i].getType().getName())
                                    .append(" ")
                                    .append(parameters[i].getName());
                        }
                        documentationGenerator.append(")</p>\n");
                    }
                }

                Method[] classMethods = documentationClass.getDeclaredMethods();
                if (classMethods.length > 0) {
                    documentationGenerator.append("<h3>Methods:</h3>\n");
                    for (Method method : classMethods) {
                        String modifiers = Modifier.toString(method.getModifiers());
                        String returnType = method.getReturnType().getName();
                        String methodName = method.getName();
                        documentationGenerator.append("<p>")
                                .append(modifiers)
                                .append(" ")
                                .append(returnType)
                                .append(" ")
                                .append(methodName)
                                .append("(");

                        Parameter[] parameters = method.getParameters();
                        for (int i = 0; i < parameters.length; i++) {
                            if (i > 0) {
                                documentationGenerator.append(", ");
                            }
                            documentationGenerator.append(parameters[i].getType().getName())
                                    .append(" ")
                                    .append(parameters[i].getName());
                        }
                        documentationGenerator.append(")</p>\n");
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String html = """
            <!DOCTYPE html>
            <html>
            <head>
            <title>Boggle Documentation</title>
            </head>
            <body>
            <h1>Boggle board game documentation</h1>
            """ + documentationGenerator.toString() + """
                </body>
                </html>
                """;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("documentation/documentation.html"))) {
            writer.write(html);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
