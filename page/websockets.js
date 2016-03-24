function updateCircle(type, value)
{
    //console.log(type)
    //console.log(value)

    if(type == 1)
    {
        circleX += value;
    }
    else if(type == 2)
    {
        circleY += value;
    }

    draw();
}

document.observe("dom:loaded", function() {
    if (!window.WebSocket) {
        alert("FATAL: WebSocket not natively supported. This demo will not work!");
    }

    var ws = new WebSocket("ws://localhost:8080");

    ws.onopen = function() {
        console.log("Connection Established");
    }

    ws.onmessage = function(e) {
        var fileReader = new FileReader();

        fileReader.onload = function(event)
        {
            var data = new DataView(event.target.result, 0);
            updateCircle(data.getInt8(0), data.getInt32(1));
        };

        fileReader.readAsArrayBuffer(e.data);
    }

    ws.onclose = function() {
        console.log("Connection Lost");
        ws = null;
    }
});
