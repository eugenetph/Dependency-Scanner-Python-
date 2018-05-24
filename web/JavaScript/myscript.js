
let filelist;
var test;
function _(e1) {
    return document.getElementById(e1);
}

$(document).ready(function () {
    document.getElementById("upload").disabled = true;
    
    $(".progress").hide();
    $("#loading").hide();
    $('.file_drag_area').on('dragover', function () {
        $(this).addClass('file_drag_over');
        return false;
    });
    $('.file_drag_area').on('dragleave', function () {
        $(this).removeClass('file_drag_over')
    });
    $('.file_drag_area').on('drop', function (e) {
        e.preventDefault();
        $(this).removeClass('file_drag_over');

        var dragAndDropFile = e.originalEvent.dataTransfer;
        
        var fileInfo = _("fileinfo");
        var text = "";
        if ('files' in dragAndDropFile && dragAndDropFile.files.length > 0) { 
            document.getElementById("upload").disabled = false;
            for (var i = 0; i < dragAndDropFile.files.length; i++) {
                text += "<br></br><strong>" + (i + 1) + ".file</strong><br></br>";
                var file = dragAndDropFile.files[i];
                
                if (file.name)
                    text += "name: " + file.name + "<br></br>";
                if ('size' in file)
                    text += "size: " + file.size + " bytes <br></br>";
            }
            
            filelist = dragAndDropFile.files;
        } else {
            if (dragAndDropFile.value == "")
                text += "Select one or more files.";
            else {
                text += "The files property is not supported by your browser!";
                text += "<br></br>The path of the selected file: " + dragAndDropFile.value;
            }
        }
        fileInfo.innerHTML = text;
    });
    $("#file").on('change', function () {
        document.getElementById("upload").disabled = false;
        var myFile = _("file");
        var fileInfo = _("fileinfo");
        test =  _("file").files[0];
        console.log("test: " + myFile);
        var text = "";
        if ('files' in myFile && myFile.files.length>0) {
                for (var i = 0; i < myFile.files.length; i++) {
                    text += "<br></br><strong>" + (i + 1) + ".file</strong><br></br>";
                    var file = myFile.files[i];
                    console.log(file);
                    if (file.name)
                        text += "name: " + file.name + "<br></br>";
                    if ('size' in file)
                        text += "size: " + file.size + " bytes <br></br>";
                }
        } else {
            document.getElementById("upload").disabled = true;
            if (myFile.value == "")
                text += "Select one or more files.";
            else {
                text += "The files property is not supported by your browser!";
                text += "<br>The path of the selected file: " + myFile.value;
            }
        }
        fileInfo.innerHTML = text;
    });
    $("#upload").on('click', function () {
        event.preventDefault();
        document.getElementById("upload").disabled = true;
        var formData = new FormData();
        if ($('#file')[0].files.length !== 0) {
            $(".progress").show();
            //loop here
            for (var i = 0; i < $('#file')[0].files.length; i++) {
                formData.append('file' + i, $('#file')[0].files[i]);
            }
        } else if(filelist !== undefined){
            for (var j = 0; j < filelist.length; j++) {
            $(".progress").show();
                formData.append('file' + j, filelist[j]);
            }
        }
        $.ajax({
            xhr: function () {
                var xhr = new window.XMLHttpRequest();
                xhr.upload.addEventListener("progress", function (event) {
                    var percent;
                    if (event.lengthComputable) {
                        console.log("Bytes Loaded: " +event.loaded);
                        console.log("Total Size: " +event.total);
                        console.log("Percentage Uploaded: " + (event.loaded/event.total));
                        percent = Math.round((event.loaded / event.total)*100);
                        $('#progressBar').attr('aria-valuenow', percent).css('width', percent + '%').text(percent + '%');
                    }
                    if(percent === 100){
                        $("#loading").show();
                    }
                });
                
                return xhr;
            },
                url: 'FileInfoServlet',
                enctype: 'multipart/form-data',
                type: 'POST',
                processData: false,
                contentType: false,
                data: formData,
                dataType: "json",
                success: function (responseData) {
                    console.log(responseData);
                    $('#ajaxGetUserServletResponse').text(responseData[0].message[0]);
                    $("#loading").hide();
                    document.getElementById("nextpage").innerHTML = "View Report";
                    document.getElementById("nextpage").href += "filehash="+ responseData[1]+ "&filename="+responseData[2]+"&option=severity&order=asc";
                    document.getElementById("upload").disabled = true;
                },
                error: function (e) {
                    $('#ajaxGetUserServletResponse').text(e.responseText);
                    $("#upload").prop("disabled", false);
                }
            });
    });
});;