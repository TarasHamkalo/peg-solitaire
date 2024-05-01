const gameName = "pegsolitaire";

const config = {
    "auth_server": "http://localhost:7070",
    "client_id": "client",
    "secret": "secret",
    "response_type": "code",
    "scope": "openid profile",
    "redirect_uri": "http://localhost:80/",
    "client_authorization": "Basic Y2xpZW50OnNlY3JldA=="
};

const authorizationUrl = new URL(config['auth_server'] + "/oauth2/authorize");
authorizationUrl.searchParams.set("client_id", config["client_id"]);
authorizationUrl.searchParams.set("secret", config["secret"]);
authorizationUrl.searchParams.set("scope", config["scope"]);
authorizationUrl.searchParams.set("response_type", config["response_type"]);
authorizationUrl.searchParams.set("redirect_uri", "http://localhost:80/");
// config["redirect_uri"]
const gameServerUrl = "http://localhost/pegsolitaire";
const movesUrl = new URL(gameServerUrl + "/moves");
const selectUrl = new URL(gameServerUrl + "/select?x=0&y=0");
const moveUrl = new URL(gameServerUrl + "/move?x=0&y=0");
const stateUrl = new URL(gameServerUrl + "/state");
const newGameUrl = new URL(gameServerUrl + "/new");

const serviceApiUrl = "http://localhost:8080/api";
const commentsApiUrl = serviceApiUrl + "/comments";