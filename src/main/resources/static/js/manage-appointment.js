$(document).ready(function () {

    var current_fs, next_fs, previous_fs;

    // No BACK button on first screen
    if ($(".show").hasClass("first-screen")) {
        $(".prev").css({'display': 'none'});
    }

    // Next button
    $(".next-button").click(function () {

        current_fs = $(this).parent().parent().parent();
        next_fs = $(this).parent().parent().parent().next();

        $(".prev").css({'display': 'block'});

        $(current_fs).removeClass("show");
        $(next_fs).addClass("show");

        $("#progressbar li").eq($(".card2").index(next_fs)).addClass("active");

        current_fs.animate({}, {
            step: function () {

                current_fs.css({
                    'display': 'none',
                    'position': 'relative'
                });

                next_fs.css({
                    'display': 'block'
                });
            }
        });
    });

});

//add task
var add_task = document.querySelector(".addtask button");
var task_input1 = document.querySelector(".addtask .addService");
var task_input2 = document.querySelector(".addtask .addPrice");

var shown = document.querySelector(".added_tasks");

var i = parseInt((document.querySelector(".added_tasks tr:last-child").id).replace("t", "")) + 1;

add_task.addEventListener('click', function () {
    var added_tr = document.querySelectorAll(".added_tasks tr");
    var task1 = document.querySelector(".addtask .addService");
    var task2 = document.querySelector(".addtask .addPrice");

    var line = document.createElement("tr");
    line.setAttribute("id", "t" + i);

    var tr_items = `
<th scope="row">${i}</th>
<td colspan="2">${task_input1.value}</td>
<td>$<span class="servicePrice">${task_input2.value}</span></td>
<td><i class="fa fa-trash" onclick="Delete(this)"></i></td>
`;

    line.innerHTML = tr_items;
    shown.appendChild(line);
    task1.value = " ";
    task2.value = " ";

    i++;

});

// delete task???
function Delete(object) {
    tr_id = object.parentNode.parentNode.id;
    var elem = document.getElementById(tr_id);
    shown.removeChild(elem);

}