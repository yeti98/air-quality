function updateLocationTable(message) {
    const tableBody = document.getElementById("location-table-body")
    tableBody.innerHTML = message
}

$(document).ready(function () {
    // Activate tooltip
    $('[data-toggle="tooltip"]').tooltip();

    // Select/Deselect checkboxes
    var checkbox = $('table tbody input[type="checkbox"]');
    $("#selectAll").click(function () {
        if (this.checked) {
            checkbox.each(function () {
                this.checked = true;
            });
        } else {
            checkbox.each(function () {
                this.checked = false;
            });
        }
    });
    checkbox.click(function () {
        if (!this.checked) {
            $("#selectAll").prop("checked", false);
        }
    });

    $(document).on("click", "#removeALocation", function () {
        const content = $(this).data('content');
        const locationId = $(this).data('location-id');
        console.log(content)
        console.log(locationId)
        $(".modal-body #mainContent").text(content);
        $(".modal-body #location-id").text(locationId);
    });

    $(document).on("click", "#editAnUser", function () {
        const name = $(this).data('user-name');
        const email = $(this).data('user-email');
        const role = $(this).data('user-role');
        const isLocked = $(this).data('user-lock');
        $("#editName").val(name);
        $("#editEmail").val(email);
        $("#editIsLocked").val(isLocked.toString());
        $("#editRole").val(role);
    });

    $("#formEditUserModal").submit(function (event) {
        event.preventDefault();

        const $form = $(this);
        const $inputs = $form.find("input, select, button, textarea");

        const name = $("#editName").val();
        const email = $("#editEmail").val();
        const isLocked = $("#editIsLocked").val();
        const role = $("#editRole").val();
        $inputs.prop("disabled", true);

        const bodyAjax = JSON.stringify(
            {
                name: name, isLocked: isLocked, role: role
            }
        )
        request = $.ajax({
            url: "/users/" + email,
            type: "put",
            data: bodyAjax,
            contentType: "application/json",
            success: function (msg) {
                $inputs.prop("disabled", false);
                window.location.reload();
            },
            error: function (msg) {
                $inputs.prop("disabled", false);
                alert("Lưu thất bại");
            }
        });
    });

    $("#formAddLocationModal").submit(function (event) {
        event.preventDefault();

        const $form = $(this);
        const $inputs = $form.find("input, select, button, textarea");

        const name = $("#addName").val();
        const locationLat = $("#addLat").val();
        const locationLong = $("#addLong").val();
        const description = $("#addDescription").val();
        $inputs.prop("disabled", true);

        const bodyAjax = JSON.stringify(
            {
                name,
                locationLat,
                locationLong,
                description
            }
        )
        console.log(bodyAjax)
        request = $.ajax({
            url: "/locations",
            type: "post",
            data: bodyAjax,
            contentType: "application/json; charset=utf-8",
            success: function (msg) {
                $inputs.prop("disabled", false);
                updateLocationTable(msg)
                $('#addLocationModal').modal('toggle');
            },
            error: function (msg) {
                $inputs.prop("disabled", false);
                alert("Thêm thất bại: \n", msg);
            }
        });
    })

    $("#formRemoveLocationModal").submit(function (event) {
        event.preventDefault();

        const $form = $(this);
        const $inputs = $form.find("input, select, button, textarea");

        const id = $("#location-id").text();

        request = $.ajax({
            url: "/locations/" + id,
            type: "delete",
            contentType: "application/json; charset=utf-8",
            success: function (msg) {
                $inputs.prop("disabled", false);
                updateLocationTable(msg)
                $('#removeLocationModal').modal('toggle');
            },
            error: function (msg) {
                $inputs.prop("disabled", false);
                alert("Xóa thất bại: \n", msg);
                window.location.reload();
            }
        });
    })
})
