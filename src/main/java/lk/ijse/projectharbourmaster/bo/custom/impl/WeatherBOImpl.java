package lk.ijse.projectharbourmaster.bo.custom.impl;

import lk.ijse.projectharbourmaster.bo.custom.WeatherBO;
import lk.ijse.projectharbourmaster.dao.DAOFactory;
import lk.ijse.projectharbourmaster.dao.custom.BoatDAO;
import lk.ijse.projectharbourmaster.dao.custom.CrewDAO;
import lk.ijse.projectharbourmaster.dao.custom.WeatherDAO;
import lk.ijse.projectharbourmaster.dao.custom.WeatherEmailDAO;
import lk.ijse.projectharbourmaster.dto.WeatherDTO;
import lk.ijse.projectharbourmaster.entity.Weather;
import lk.ijse.projectharbourmaster.entity.Weather_email;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WeatherBOImpl implements WeatherBO {

    WeatherDAO weatherDAO = (WeatherDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.WEATHER);
    WeatherEmailDAO weatherEmailDAO = (WeatherEmailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.WEATHER);
    CrewDAO crewDAO = (CrewDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CREW);
    BoatDAO boatDAO = (BoatDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.BOAT);

    @Override
    public boolean isUpdatableDay(String nextDate) throws SQLException {
        return weatherEmailDAO.isUpdatableDay(nextDate);

    }

    @Override
    public boolean addWeatherMgDates(String nextDate) throws SQLException, IOException {
        return weatherEmailDAO.add(new Weather_email(nextDate));

    }

    @Override
    public boolean addWeatherRecords(WeatherDTO weatherDTO) throws SQLException, IOException {
        return weatherDAO.add(
                new Weather(
                        weatherDTO.getUserId(),
                        weatherDTO.getWindSpeed(),
                        weatherDTO.getSpecialCauses(),
                        weatherDTO.getDate(),
                        weatherDTO.getTime()
                )
        );

    }

    @Override
    public List<WeatherDTO> getAllWeatherRecords() throws SQLException {
        List<WeatherDTO> weatherDTOList = new ArrayList<>();

        for (Weather weather : weatherDAO.getAll()) {
            double windSpeed = weather.getWindSpeed();
            String specialCauses = weather.getSpecialCauses();

            String threatLevel = "";

            if (windSpeed < 37) {
                threatLevel = "stable";
            } else if (windSpeed < 62) {
                threatLevel = "low";
            } else if (windSpeed < 88) {
                threatLevel = "medium";
            } else if (windSpeed < 118) {
                threatLevel = "high";
            } else {
                threatLevel = "critical";
            }

            if (specialCauses != null && !specialCauses.equals("")) {
                threatLevel = "critical";
            }

            weatherDTOList.add(
                    new WeatherDTO(
                            weather.getUserId(),
                            weather.getWindSpeed(),
                            weather.getSpecialCauses(),
                            weather.getDate(),
                            weather.getTime(),
                            threatLevel
                    )
            );
        }
        return weatherDTOList;

    }

    @Override
    public List<String> getAllEmailsCrew() throws SQLException {
        return crewDAO.getAllEmails();

    }

    @Override
    public List<String> getAllEmailsBoat() throws SQLException {
        return boatDAO.getAllEmails();

    }

    @Override
    public String searchWeatherSpecialCauses(String date) throws SQLException {
        return weatherDAO.searchWeatherSpecialCauses(date);

    }

    @Override
    public boolean updateWeather(WeatherDTO weatherDTO, String date, String time) throws SQLException {
        return weatherDAO.updateWeather(
                new Weather(
                        weatherDTO.getUserId(),
                        weatherDTO.getWindSpeed(),
                        weatherDTO.getSpecialCauses(),
                        weatherDTO.getDate(),
                        weatherDTO.getTime()
                ),
                date,
                time
        );

    }

    @Override
    public WeatherDTO searchWeather(String userId, String date) throws SQLException {
        Weather weather = weatherDAO.searchWeather(userId, date);
        return new WeatherDTO(
                weather.getUserId(),
                weather.getWindSpeed(),
                weather.getSpecialCauses(),
                weather.getDate(),
                weather.getTime(),
                null
        );
    }

    @Override
    public boolean removeWeather(WeatherDTO weatherDTO) throws SQLException {
        return weatherDAO.removeWeather(
                new Weather(
                        weatherDTO.getUserId(),
                        weatherDTO.getWindSpeed(),
                        weatherDTO.getSpecialCauses(),
                        weatherDTO.getDate(),
                        weatherDTO.getTime()
                )
        );

    }

}
