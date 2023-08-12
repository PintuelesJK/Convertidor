import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class ConvetidorMoneda {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CurrencyConverterFrame frame = new CurrencyConverterFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

class CurrencyConverterFrame extends JFrame {
    private final CurrencyConverter converter;

    private final JTextField inputField;
    private final JComboBox<String> fromCurrencyComboBox;
    private final JComboBox<String> toCurrencyComboBox;
    private final JLabel resultLabel;

    public CurrencyConverterFrame() {
        setTitle("Convertidor de Moneda");
        setSize(800, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2));

        converter = new CurrencyConverter();
        inputField = new JTextField();
        fromCurrencyComboBox = new JComboBox<>(Currency.getCurrencyNames());
        toCurrencyComboBox = new JComboBox<>(Currency.getCurrencyNames());
        resultLabel = new JLabel("", SwingConstants.CENTER);

        JButton convertButton = new JButton("Convert");
        convertButton.addActionListener(new ConvertButtonListener());

        add(new JLabel("Amount:"));
        add(inputField);
        add(new JLabel("From Currency:"));
        add(fromCurrencyComboBox);
        add(new JLabel("To Currency:"));
        add(toCurrencyComboBox);
        add(convertButton);

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.add(resultLabel, BorderLayout.CENTER);
        add(resultPanel);
    }

    private class ConvertButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                double amount = Double.parseDouble(inputField.getText());
                String fromCurrency = (String) fromCurrencyComboBox.getSelectedItem();
                String toCurrency = (String) toCurrencyComboBox.getSelectedItem();
                double result = converter.convert(amount, fromCurrency, toCurrency);
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                resultLabel.setText(decimalFormat.format(amount) + " " + fromCurrency + " = " + decimalFormat.format(result) + " " + toCurrency);
            } catch (NumberFormatException ex) {
                resultLabel.setText("Invalid input");
            }
        }
    }
}

class Currency {
    private final String code;
    private final String name;

    public Currency(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return code + " - " + name;
    }

    public static String[] getCurrencyNames() {
        Currency[] currencies = {
            new Currency("USD", "US Dollar"),
            new Currency("EUR", "Euro"),
            new Currency("JPY", "Japanese Yen"),
            new Currency("GBP", "British Pound"),
            new Currency("MXN", "Mexican Peso")
        };
        String[] currencyNames = new String[currencies.length];
        for (int i = 0; i < currencies.length; i++) {
            currencyNames[i] = currencies[i].toString();
        }
        return currencyNames;
    }
}

class CurrencyConverter {
    private static final double[] exchangeRates = {1.0, 0.85, 109.83, 0.76, 23.95}; // Exchange rates as of the knowledge cutoff date (September 2021)

    public double convert(double amount, String fromCurrency, String toCurrency) {
        int fromIndex = getCurrencyIndex(fromCurrency);
        int toIndex = getCurrencyIndex(toCurrency);
        return amount * exchangeRates[toIndex] / exchangeRates[fromIndex];
    }

    private int getCurrencyIndex(String currencyCode) {
        for (int i = 0; i < Currency.getCurrencyNames().length; i++) {
            if (Currency.getCurrencyNames()[i].startsWith(currencyCode)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Invalid currency code: " + currencyCode);
    }
}
