import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class WeatherApp extends JFrame {
    private JTextField cityInput;
    private JButton getWeatherBtn;
    private JTextArea resultArea;

    private final String API_KEY = "64709b9a0c348ec5176e6fa4fa6d6129"; // Replace with your API Key

    public WeatherApp() {
        setTitle("Weather App 🌤");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        JLabel label = new JLabel("Enter City: ");
        cityInput = new JTextField(15);
        getWeatherBtn = new JButton("Get Weather");

        topPanel.add(label);
        topPanel.add(cityInput);
        topPanel.add(getWeatherBtn);

        resultArea = new JTextArea();
        resultArea.setEditable(false);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        add(panel);

        getWeatherBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String city = cityInput.getText().trim();
                if (!city.isEmpty()) {
                    getWeather(city);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a city name!");
                }
            }
        });
    }

    private void getWeather(String city) {
        try {
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q="
                                + city + "&appid=" + API_KEY + "&units=metric";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject json = new JSONObject(response.toString());
            String name = json.getString("name");
            double temp = json.getJSONObject("main").getDouble("temp");
            String condition = json.getJSONArray("weather").getJSONObject(0).getString("description");
            int humidity = json.getJSONObject("main").getInt("humidity");
            double wind = json.getJSONObject("wind").getDouble("speed");

            resultArea.setText("Weather in " + name + ":
"
                    + "--------------------------
"
                    + "🌡 Temperature: " + temp + "°C\n"
                    + "☁ Condition: " + condition + "\n"
                    + "💧 Humidity: " + humidity + "%\n"
                    + "🌬 Wind: " + wind + " m/s\n");
        } catch (Exception ex) {
            resultArea.setText("❌ Error: Could not retrieve weather data.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WeatherApp().setVisible(true);
            }
        });
    }
}