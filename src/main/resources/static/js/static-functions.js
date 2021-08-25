function search() {
  if($('#tagFilters .search-tag.active').length)
    $('#searchFilterBtn').addClass('active');
  let searchTerm = $('#searchBox').val();
  let dateFilterBegin = $('#dateFilterBegin').val()
  let dateFilterEnd = $('#dateFilterBegin').val()
  let tags = $('#tagFilters .search-tag.active').map(function(){
    return $(this).data('target');
  }).get();
  $.ajax({
    url: contextpath + 'search/meals',
    method: "GET",
    data: {
      searchTerm: searchTerm,
      tags: tags
    },
    beforeSend: function() {
      $('#mealTable').addClass('loading');
    },
    success: function (data) {
      $('#mealTable').html(data);
      $('#mealTable').removeClass('loading');
      cachedCheckedMealIds.forEach((id) => {
        $('#mealCheckbox_' + id).prop('checked',true);
        $('#sideAlert_' + id).addClass('show');
      });
    }
  });
}
function addSelectedMealsToWeek() {
  let weekId = parseInt($('#addMealsWeekSelect').val());
  let addIndex = $('#addMealsWeekSelect option:selected').data('add-index');
  $('#addMealsToWeekButtonContainer').hide();
  $('#addMealsToWeekCheckboxContainer').html($('#checkmarkAni').html());
  $('#addMealsToWeekCheckboxContainer').show();
  $('input.meal-select:checkbox').prop('checked', false);
  $('.add-side-alert ').removeClass('show');
  setTimeout(function(){
    $('.alert.add-meal').removeClass('display');
    setTimeout(function(){
      $('#addMealsToWeekCheckboxContainer').hide();
      $('#addMealsToWeekButtonContainer').show();
    },1000)
  },2000)
  $.ajax({
    url: contextpath + 'week/add-meal',
    method: "POST",
    dataType: "json",
    data: {
      weekId: weekId,
      meals: JSON.stringify(Array.from(cachedCheckedMealIds)),
      mealSidesMap: JSON.stringify(mealSidesMap),
      addIndex: addIndex
    },
    success: function (data) {
      refreshMealList();
      refreshWeekList();
      $('#addMealsWeekSelect option:selected').val(data.id);
      cachedCheckedMealIds = new Set();
      mealSidesMap = {};
    }
  });
}

function removeMealFromWeek(mealId, weekId) {
  $.ajax({
    url: contextpath + 'meal/remove-from-week',
    method: "DELETE",
    data : {
      mealId: mealId,
      weekId: weekId
    },
    success: function (data) {
      $('#weekAccordion_' + weekId + ' [id^=accordion_header_sub_' + mealId + ']').remove();
      $('#weekAccordion_' + weekId + ' .accordion-sub-' + mealId).remove();
      refreshMeal(mealId);
    }
  });
}

function deleteWeek(id) {
  if (confirm('Are you sure you want to delete this week?')) {
    $.ajax({
      url: contextpath + 'week/delete/' + id,
      method: "DELETE",
      success: function (data) {
        $('#weekAccordion_' + id).remove();
        $('#weekAccordionHeader_' + id).remove();
        $('#addMealsWeekSelect option[value="' + id + '"]').remove();
      }
    });
  }
}

function deleteSide(id) {
  if (confirm('Are you sure you want to delete this side? This side will also be removed from all planned meals.')) {
    $.ajax({
      url: contextpath + 'side/delete/' + id,
      method: "DELETE",
      success: function (data) {
        refreshSides();
        $('#sideAccordionHeader_' + id).remove();
        $('#sideAccordion_' + id).remove();
        // $('[id^=accordion_header_sub_' + id + ']').remove();
        // $('.accordion-sub-' + id).remove();
      }
    });
  }
}

function deleteMeal(id) {
  if (confirm('Are you sure you want to delete this meal? This meal will also be removed from all planned meals.')) {
    $.ajax({
      url: contextpath + 'meal/delete/' + id,
      method: "DELETE",
      success: function (data) {
        $('#accordionHeader_' + id).remove();
        $('#accordion_' + id).remove();
        $('[id^=accordion_header_sub_' + id + ']').remove();
        $('.accordion-sub-' + id).remove();
      }
    });
  }
}

function refreshMealList() {
  search();
}

function refreshWeekList() {
  $.ajax({
    url: contextpath + 'weeks',
    method: "GET",
    beforeSend: function() {
      $('#weekTable').addClass('loading');
    },
    success: function (data) {
      $('#weekTable').removeClass('loading');
      $('#weekTable').html(data);
      $('.sub-meal-list').unbind();
      $('.sub-meal-list').listSwipe();
    }
  });
}

function refreshSideList() {
  $.ajax({
    url: contextpath + 'sides',
    method: "GET",
    beforeSend: function() {
      $('#sideTable').addClass('loading');
    },
    success: function (data) {
      $('#sideTable').removeClass('loading');
      $('#sideTable').html(data);
    }
  });
}

function refreshMeal(id) {
  refreshMealDetails(id);
  refreshMealSubDetails(id);
  refreshMealName(id);
}

function refreshMealDetails(mealId) {
  $.ajax({
    url: contextpath + 'meal/details/' + mealId + '?view=dynamic/meal-details',
    method: "GET",
    beforeSend: function() {
      $('#accordion_' + mealId).addClass('loading');
    },
    success: function (data) {
      $('#accordion_' + mealId).replaceWith(data);
      $('#accordion_' + mealId).removeClass('loading');
    }
  });
}
function refreshMealSubDetails(mealId) {
  $.ajax({
    url: contextpath + 'meal/details/' + mealId + '?view=dynamic/meal-sub-details',
    method: "GET",
    beforeSend: function() {
      $('.accordion-sub-' + mealId).addClass('loading');
    },
    success: function (data) {
      $('.accordion-sub-' + mealId).replaceWith(data);
      $('.accordion-sub-' + mealId).removeClass('loading');
    }
  });
}
function refreshMealName(mealId) {
  $.ajax({
    url: contextpath + 'json/meal/' + mealId,
    method: "GET",
    success: function (data) {
      $('#mealName_' + mealId).html(data.name);
      $('[id^=subMealName_' + mealId +']').html(data.name);
    }
  });
}
function refreshWeek(id) {
  $.ajax({
    url: contextpath + 'week/details/' + id + '?view=dynamic/week-details',
    method: "GET",
    success: function (data) {
      $('#weekAccordion_' + id).replaceWith(data);
      $('#weekAccordionHeader_' + id).click();
      $('.sub-meal-list').unbind();
      $('.sub-meal-list').listSwipe();
    }
  });
}

function refreshTags() {
  $.ajax({
    url: contextpath + 'json/tags',
    method: "GET",
    success: function (data) {
      tags = new Map(Object.entries(data))
    }
  });
}

function refreshSides() {
  $.ajax({
    url: contextpath + 'json/sides',
    method: "GET",
    success: function (data) {
      sides = data;
    }
  });
}

function refreshIngredients() {
  $.ajax({
    url: contextpath + 'json/ingredients',
    method: "GET",
    success: function (data) {
      ingredients = data;
    }
  });
}
