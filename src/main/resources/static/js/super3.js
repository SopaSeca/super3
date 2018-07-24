var invertedColors = [];

function invertColor(hex) {
    if (invertedColors[hex] != null) {
        return invertedColors[hex];
    }
    if (hex.indexOf('#') === 0) {
        hex = hex.slice(1);
    }
    // convert 3-digit hex to 6-digits.
    if (hex.length === 3) {
        hex = hex[0] + hex[0] + hex[1] + hex[1] + hex[2] + hex[2];
    }
    if (hex.length !== 6) {
        throw new Error('Invalid HEX color.');
    }
    // invert color components
    var r = (255 - parseInt(hex.slice(0, 2), 16)).toString(16),
        g = (255 - parseInt(hex.slice(2, 4), 16)).toString(16),
        b = (255 - parseInt(hex.slice(4, 6), 16)).toString(16);
    // pad each with zeros and return
    var result = '#' + padZero(r) + padZero(g) + padZero(b);
    invertedColors[hex] = result;
    return result;
}

function padZero(str, len) {
    len = len || 2;
    var zeros = new Array(len).join('0');
    return (zeros + str).slice(-len);
}

function checkBoard(data) {
    var board = data.result;
    var eRollDice = $("#rollDice");
    var dice = board.dice;
    eRollDice.off("click");
    eRollDice.html("");
    if (board != null) {
        if (board.activePlayer != null) {
            $("#activePlayer").html("Player " + board.activePlayer.name);
            if (data.myTurn && (dice == null || !dice.locked)) {
                eRollDice.click(function () {
                    rollDice();
                });
                eRollDice.html("<div class='button'>Roll</div>");
            }
        }
        if (dice != null) {
            var dice1 = $("#dice1");
            var dice2 = $("#dice2");
            dice1.show();
            dice2.show();
            dice1.html(dice.value1);
            dice2.html(dice.value2);
        } else {
            $("#dice1").hide();
            $("#dice2").hide();
        }
        var areas = board.cells;
        if (areas != null) {
            var i;
            for (i = 0; i < areas.length; i++) {
                var area = areas[i];
                var areaPlayer = area.player;
                var eArea = $("#area-" + area.number);
                if (eArea != null) {
                    if (areaPlayer != null) {
                        var invertedAreaColor = invertColor(areaPlayer.color);
                        eArea.css({
                            "background-color": areaPlayer.color,
                            "color": invertedAreaColor,
                            "border-color": invertedAreaColor
                        });
                    }
                }
                var pins = area.cells;
                if (pins != null) {
                    var j;
                    for (j = 0; j < pins.length; j++) {
                        var pin = pins[j];
                        var ePin = $("#pin-" + area.number + "-" + pin.number);
                        if (ePin != null) {
                            ePin.off("click");
                            ePin.removeClass("disabled");
                            var pinPlayer = pin.player;
                            if (pinPlayer != null) {
                                var invertedPinColor = invertColor(pinPlayer.color);
                                ePin.css({
                                    "background-color": pinPlayer.color,
                                    "color": invertedPinColor,
                                    "box-shadow": "none"
                                });
                                ePin.prop("title", area.number + "-" + pin.number + ": " + pinPlayer.name);
                            } else {
                                ePin.prop("title", area.number + "-" + pin.number);
                            }
                            var areaNumber = parseInt(area.number);
                            var pinNumber = parseInt(pin.number);
                            var enable = false;
                            if (dice != null) {
                                var value = parseInt(dice.value1) + parseInt(dice.value2);
                                if (value === 2) {
                                    enable = pinPlayer != null;
                                } else if (pinPlayer == null) {
                                    if (value === 12) {
                                        enable = true;
                                    } else {
                                        if (pinNumber === value || areaNumber === value) {
                                            if (value !== 7 || pinNumber === 7) {
                                                enable = true;
                                            }
                                        }
                                    }
                                }
                            }
                            if (enable) {
                                (function () {
                                    // mutable-variable-is-accessible-from-closure
                                    var area = areaNumber;
                                    var pin = pinNumber;
                                    ePin.click(function () {
                                        setPin(area, pin);
                                    });
                                })();
                            } else {
                                ePin.addClass("disabled");
                            }
                        }
                    }
                }
            }
        }
    }
}

function setPin(area, pin) {
    console.log("Set pin @" + area + "-" + pin);
    $.get("/rest/super3/board/pin?area=" + area + "&pin=" + pin, function (data) {
        checkBoard(data);
    });
}

function rollDice() {
    $.get("/rest/super3/board/roll", function (data) {
        checkBoard(data);
    });
}

function doPoll() {
    $.get("/rest/super3/board/", function (data) {
        checkBoard(data);
        setTimeout(doPoll, 5000);
    });
}