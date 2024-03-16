package com.cactus.weather.service.model.response_model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeatherModel {

	private Coord coord;

	private List<Weather> weather;

	private String base;

	private Main main;

	private Integer visibility;

	private Wind wind;

	private Rain rain;

	private Clouds clouds;

	private Long dt;

	private Sys sys;

	private Long timezone;

	private Long id;

	private String name;

	private Integer cod;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Coord {

		private Double lon;

		private Double lat;

	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Weather {

		private Integer id;

		private String main;

		private String description;

		private String icon;

	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Main {

		private Double temp;

		private Double feels_like;

		private Double temp_min;

		private Double temp_max;

		private Integer pressure;

		private Integer humidity;

		@JsonProperty("sea_level")
		private Integer seaLevel;

		@JsonProperty("grnd_level")
		private Integer grndLevel;

	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Wind {

		private Double speed;

		private Integer deg;

		private Double gust;

	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Rain {

		@JsonProperty("1h")
		private Double _1h;

	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Clouds {

		private Integer all;

	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Sys {

		private Integer type;

		private Integer id;

		private String country;

		private Integer sunrise;

		private Integer sunset;

	}

}
