package io.github.gabrielnavas.book_network_api.configs;

public class Configs {
    public static final int SERVER_PORT = 8088;

    public static final String HEADER_PARAM_AUTHORIZATION = "Authorization";
    public static final String HEADER_PARAM_ORIGIN = "Origin";

    public static final String ORIGIN_LOCALHOST = "http://localhost:" + Configs.SERVER_PORT;
    public static final String ORIGIN_WRONG = "http://www.uol.com.br";
}
