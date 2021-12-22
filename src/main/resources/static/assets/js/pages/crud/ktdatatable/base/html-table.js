'use strict';
// Class definition

var KTDatatableHtmlTableDemo = function() {
  // Private functions

  var bucket = function() {

    var bucket_datatable = $('#kt_datatable_bucket').KTDatatable({
      data: {
        saveState: {cookie: false},
        pageSize: 100
      },
      search: {
        input: $('#kt_datatable_bucket_search_query'),
        key: 'generalSearch',
      },
      layout: {
        class: 'datatable-bordered',
      },
      columns: [
        {
          field: '이름',
          type: 'string',
          width: 900
        },
        {
          field: '생성일',
          type: 'date',
          format: 'YYYY-MM-DD',
          width: 200
        },
      ],
    });

  };

  var object = function() {

    var object_datatable = $('#kt_datatable_object').KTDatatable({
      data: {
        saveState: {cookie: false},
        pageSize: 100
      },
      search: {
        input: $('#kt_datatable_object_search_query'),
        key: 'generalSearch',
      },
      layout: {
        class: 'datatable-bordered',
      },
      columns: [
        {
          field: '이름',
          type: 'string',
          width: 600
        },
        {
          field: '유형',
          type: 'string',
          width: 30
        },
        {
          field: '마지막 수정',
          type: 'date',
          format: 'YYYY-MM-DD',
          width: 140
        },
        {
          field: '크기',
          type: 'number',
          width: 100
        }
      ],
    });

  };

  // demo initializer
  var demo = function() {

    var datatable = $('#kt_datatable').KTDatatable({
      data: {
        saveState: {cookie: false},
      },
      search: {
        input: $('#kt_datatable_search_query'),
        key: 'generalSearch',
      },
      layout: {
        class: 'datatable-bordered',
      },
      columns: [
        {
          field: 'DepositPaid',
          type: 'number',
        },
        {
          field: 'OrderDate',
          type: 'date',
          format: 'YYYY-MM-DD',
        }, {
          field: 'Status',
          title: 'Status',
          autoHide: false,
          // callback function support for column rendering
          template: function(row) {
            var status = {
              ACTIVE: {
                'title': 'ACTIVE',
                'class': ' label-light-warning',
              },
              INACTIVE: {
                'title': 'INACTIVE',
                'class': ' label-light-danger',
              },
              // 3: {
              //   'title': 'Canceled',
              //   'class': ' label-light-primary',
              // },
              // 4: {
              //   'title': 'Success',
              //   'class': ' label-light-success',
              // },
              // 5: {
              //   'title': 'Info',
              //   'class': ' label-light-info',
              // },
              // 6: {
              //   'title': 'Danger',
              //   'class': ' label-light-danger',
              // },
              // 7: {
              //   'title': 'Warning',
              //   'class': ' label-light-warning',
              // },
            };
            return '<span class="label font-weight-bold label-lg' + status[row.Status].class + ' label-inline">' + status[row.Status].title + '</span>';
          },
        }, {
          field: 'Type',
          title: 'Type',
          autoHide: false,
          // callback function support for column rendering
          template: function(row) {
            var status = {
              1: {
                'title': 'Online',
                'state': 'danger',
              },
              2: {
                'title': 'Retail',
                'state': 'primary',
              },
              3: {
                'title': 'Direct',
                'state': 'success',
              },
            };
            return '<span class="label label-' + status[row.Type].state + ' label-dot mr-2"></span><span class="font-weight-bold text-' + status[row.Type].state + '">' + status[row.Type].title + '</span>';
          },
        },
      ],
    });

    $('#kt_datatable_search_status').on('change', function() {
      datatable.search($(this).val().toLowerCase(), '상태');
    });

    $('#kt_datatable_search_type').on('change', function() {
      datatable.search($(this).val().toLowerCase(), 'Type');
    });

    $('#kt_datatable_search_status, #kt_datatable_search_type').selectpicker();

  };

  return {
    // Public functions
    init: function() {
      // init dmeo
      demo();
      bucket();
      object();
    },
  };
}();

jQuery(document).ready(function() {
  KTDatatableHtmlTableDemo.init();
});
