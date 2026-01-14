function fn() {
    var env = karate.env; // get java system property 'karate.env'
    karate.log('karate.env system property was:', env);

    if (!env) {
        env = 'dev';
    }

    var config = {
        appUrl: 'http://localhost:8080/api'
    };

    if (env == 'dev') {
        config.appUrl = 'http://localhost:8080/api';
    } else if (env == 'test') {
        config.appUrl = 'http://localhost:8080/api';
    }

    karate.configure('connectTimeout', 5000);
    karate.configure('readTimeout', 5000);

    return config;
}
