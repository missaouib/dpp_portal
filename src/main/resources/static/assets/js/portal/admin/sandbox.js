var AdminSandbox = function (idx) {

    var csrf = {
        token : $("meta[name='_csrf']").attr("content"),
        header : $("meta[name='_csrf_header']").attr("content")
    }

    this.init = function () {
        this.handler();
    }

    this.handler = function () {

        $(".card-footer").on("click", ".accept-sandbox-button", function (e) {
            e.preventDefault();
            if(!confirm("승인 하시겠습니까?")) {
                return;
            }
            $.blockPage.full();
            sandbox.accept(idx);
        });

        $(".card-footer").on("click", ".reject-sandbox-button", function (e) {
            e.preventDefault();
            if(!confirm("반려 하시겠습니까?")) {
                return;
            }
            $.blockPage.full();
            sandbox.reject(idx);
        });
    }

    var sandbox = {

        /**
         * accept sandbox & create sandbox
         * @param idx
         */
        accept : function (idx) {

            var param = {}

            jQuery.ajax({
                url: '/admin/sandbox/' + idx + '/status/accept'
                , type: "PUT"
                , dataType: "json"
                , data: param
                , async: false
                , beforeSend: function (xhr) {
                    xhr.setRequestHeader(csrf.header, csrf.token);
                }
                , timeout: 100000
                , success: function (res) {
                    location.reload();
                }
                , error: function (res) {
                    $.blockPage.remove();
                }
            });
        },

        /**
         * reject sandbox
         * @param idx
         */
        reject : function (idx) {

            var param = {}

            jQuery.ajax({
                url: '/admin/sandbox/' + idx + '/status/reject'
                , type: "PUT"
                , dataType: "json"
                , data: param
                , async: false
                , beforeSend: function (xhr) {
                    xhr.setRequestHeader(csrf.header, csrf.token);
                }
                , timeout: 100000
                , success: function (res) {
                    location.reload();
                }
                , error: function (res) {
                    $.blockPage.remove();
                }
            });
        },

    }

    this.init();
}