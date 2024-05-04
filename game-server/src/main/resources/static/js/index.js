$(document).ready(function () {

    if (!userHasToBeAuthenticated()) {
        document.getElementById("login").textContent = "Logout";
    }

    $('#login').click(function (e) {
        e.preventDefault();

        if (userHasToBeAuthenticated()) {
            refreshTokenIfRequired();
        } else {
            logout();
            document.getElementById("login").textContent = "Login";
        }

    });

})