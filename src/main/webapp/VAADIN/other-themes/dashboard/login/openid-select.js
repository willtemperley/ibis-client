 (function($) {
  $.fn.openid = function(opt) {
    var gprovider;
    var INPUTID = 'openid_username';
    var inputarea = $('#openid_inputarea').length ? $('#openid_inputarea'): $('<div id="openid_inputarea" />');
    var messagearea = $('#openid_authmessage');
  
    var defaults = {
      txt: {
        label: 'Enter your {provider} {username}',
        username: 'username',
        title: 'Select your openID provider',
        sign: 'Sign-In'
      },
      /*
       Default providers with url. "big" variable means that icon
       will be big.
      */
      providers: [
        {
          name: 'Google',
          url: 'https://www.google.com/accounts/o8/id',
          label: null,
          big: true
        },
        {
          name: 'Yahoo',
          url: 'Yahoo',
          label: null,
          big: true
        },    
        {
          name: 'MyOpenID',
          url: 'http://{username}.myopenid.com/',
          big: true
        },
        {
          name: 'StackExchange',
          url: 'https://openid.stackexchange.com/',
          label: null,
          big: true,
          disabled: true
        }
      ],
      cookie_expires: 6 * 30, // in days.
      cookie_path: '/',
      img_path: '/img/'
    };
  
    var getBox = function(provider, idx, box_size) {
      var a = $('<a title="' + provider + '" href="#" id="btn_' + idx + 
                '" class="openid_' + box_size + '_btn ' + provider + '" />');
      return a.click(signIn);
    };
  
    var setCookie = function(value) {
      var date = new Date();
      date.setTime(date.getTime() + (settings.cookie_expires * 24 * 60 * 60 * 1000));
      document.cookie = "openid_prov=" + value + "; expires=" + date.toGMTString() + 
                        "; path=" + settings.cookie_path;
    };
  
    var readCookie = function(){
      var c = document.cookie.split(';');
      for(i in c){
        if ((pos = c[i].indexOf("openid_prov=")) != -1) 
          return $.trim(c[i].slice(pos + 12));
      }
    };
  
    var signIn = function(obj, tidx) {
      var idx = $(tidx || this).attr('id').replace('btn_', '');
      if (!(gprovider = settings.providers[idx]))
        return;
    
      // Highlight
      if (highlight = $('#openid_highlight'))
        highlight.replaceWith($('#openid_highlight a')[0]);
   
      $('#btn_' + idx).wrap('<div id="openid_highlight" />');
      setCookie(idx);
      
      if (gprovider.disabled) {
    	  messagearea.text("Provider not currently supported.");
    	  return;
      }
  
      // prompt user for input?
      showInputBox();
      if (gprovider.label === null) {
        inputarea.text(settings.txt.title);
        if (!tidx) {
          inputarea.fadeOut();
          form.submit();
        }
      }
      return false;
    };
  
    var showInputBox = function() {
      var lbl = (gprovider.label || settings.txt.label).replace(
        '{username}', (gprovider.username_txt !== undefined) ? gprovider.username_txt: settings.txt.username
      ).replace('{provider}', gprovider.name);
  
      inputarea.empty().show().append('<span class="oidlabel">' + lbl + '</span><input id="' + INPUTID + '" type="text" ' +
        ' name="username_txt" class="Verisign"/><input type="submit" value="' + settings.txt.sign + '"/>');
  
      $('#' + INPUTID).focus();
    };
  
    var submit = function(){
      var prov = (gprovider.url) ? gprovider.url.replace('{username}', $('#' + INPUTID).val()): $('#' + INPUTID).val();
      form.append($('<input type="hidden" name="url" value="' + prov + '" />'));
      form.append($('<input type="hidden" name="referrer" value="' + loginReturnTo + '" />'));
      form.append($('<input type="hidden" name="servlet_url" value="' + window.location.href + '" />'));
//      form.append($('<input type="hidden" name="op" value="' + prov + '" />'));
      form.append($('<input type="hidden" name="op" value="' + gprovider.name + '" />'));
    };
  
    var settings = $.extend(defaults, opt || {});
    var btns = $('<div id="openid_btns" />');
  
    // Add box for each provider
    var addbr = true;
    $.each(settings.providers, function(i, val) {
      if (val.big == null) {
    	  val.big = false;
      }
      if (!val.big && addbr) {
        btns.append('<br />');
        addbr = false;
      }
      btns.append(getBox(val.name, i, (val.big) ? 'large': 'small'));
    });
  
    var form = this;
    form.css({'background-image': 'none'});
    form.append(btns).submit(submit);
    btns.append(inputarea);
  
    if (idx = readCookie())
      signIn(null, '#btn_' + idx);
    else
  	inputarea.text(settings.txt.title).show();
  
    return this;
  };
})(jQuery); 