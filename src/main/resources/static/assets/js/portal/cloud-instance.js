
var CloudInstance = function (idx=0, type="ec2") {

    var csrf = {
        token : $("meta[name='_csrf']").attr("content"),
        header : $("meta[name='_csrf_header']").attr("content")
    }

    this.init = function () {
        this.handler();
    }

    this.handler = function () {

        $(".card-body, .card-header").on("click", ".sync-sandbox-instance", function (e){
            e.preventDefault();
            //$.blockPage.single("kt_datatable");
            $.blockPage.full();
            instance.sync();
        });

        $(".card-footer").on("click", ".start-instance-button", function (e) {
            e.preventDefault();
            if(!confirm("시작 하시겠습니까?")) {
                return;
            }
            $.blockPage.full();
            instance.start(idx);
        });

        $(".card-footer").on("click", ".stop-instance-button", function (e) {
            e.preventDefault();
            if(!confirm("중지 하시겠습니까?")) {
                return;
            }
            $.blockPage.full();
            instance.stop(idx);
        });

        $(".card-footer").on("click", ".terminate-instance-button", function (e) {
            e.preventDefault();
            if(!confirm("종료 하시겠습니까?")) {
                return;
            }
            $.blockPage.full();
            instance.terminate(idx);
        });

        $(".card-footer").on("click", ".start-sagemaker-button", function (e) {
            e.preventDefault();
            if(!confirm("시작 하시겠습니까?")) {
                return;
            }
            $.blockPage.full();
            sagemaker.start(idx);
        });

        $(".card-footer").on("click", ".stop-sagemaker-button", function (e) {
            e.preventDefault();
            if(!confirm("중지 하시겠습니까?")) {
                return;
            }
            $.blockPage.full();
            sagemaker.stop(idx);
        });

        $(".card-footer").on("click", ".terminate-sagemaker-button", function (e) {
            e.preventDefault();
            if(!confirm("종료 하시겠습니까?")) {
                return;
            }
            $.blockPage.full();
            sagemaker.terminate(idx);
        });

    }

    var instance = {

        /**
         * Cloud Instance Start
         * @param idx
         */
        start : function (idx) {

            var param = {
                idx : idx
            }

            jQuery.ajax({
                url: '/admin/cloud-instance/ec2/start'
                , type: "POST"
                , dataType: "json"
                , data: param
                , async : false
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
         * Cloud Instance Stop
         * @param idx
         */
        stop : function (idx) {

            var param = {
                idx : idx
            }

            jQuery.ajax({
                url: '/admin/cloud-instance/ec2/stop'
                , type: "POST"
                , dataType: "json"
                , data: param
                , async : false
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
         * CloudInstance Terminate
         * @param idx
         */
        terminate : function (idx) {

            var param = {
                idx : idx
            }

            jQuery.ajax({
                url: '/admin/cloud-instance/ec2/terminate'
                , type: "POST"
                , dataType: "json"
                , data: param
                , async : false
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

        sync : function () {
            var param = {
            }
            jQuery.ajax({
                url: '/admin/cloud-instance/sync'
                , type: "POST"
                , dataType: "json"
                , data: param
                , async : false
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
        }

    }

    var sagemaker = {

        /**
         * Cloud Instance Start
         * @param idx
         */
        start : function (idx) {

            var param = {
                idx : idx
            }

            jQuery.ajax({
                url: '/admin/cloud-instance/sagemaker/start'
                , type: "POST"
                , dataType: "json"
                , data: param
                , async : false
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
         * Cloud Instance Stop
         * @param idx
         */
        stop : function (idx) {

            var param = {
                idx : idx
            }

            jQuery.ajax({
                url: '/admin/cloud-instance/sagemaker/stop'
                , type: "POST"
                , dataType: "json"
                , data: param
                , async : false
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
         * CloudInstance Terminate
         * @param idx
         */
        terminate : function (idx) {

            var param = {
                idx : idx
            }

            jQuery.ajax({
                url: '/admin/cloud-instance/sagemaker/terminate'
                , type: "POST"
                , dataType: "json"
                , data: param
                , async : false
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