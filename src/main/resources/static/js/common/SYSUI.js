function SYSUI(options) {
	this._initial(options);
};
SYSUI.prototype = {
	constructor: this,
	_initial:function(options){
		var par = {
			Module:null,
			SYSframe:null,
			Method:null,
			skin:null,
			Mystorage:null,
			sessionArray:[],
			mouIconClose: '&#xe630', //箭头
			mouIconOpen: '&#xe615', // 箭头
			JsFile:null,//扩展js的文件
			windowmethod:function(){},
			expandmethod:function(){}//自定义扩展
		}
		//封装extend合并方法
	    this.extend=function(o, n, override) {
			for(var key in n) {
				if(n.hasOwnProperty(key) && (!o.hasOwnProperty(key) || override)) {
					o[key] = n[key];
				}
			}
			return o;
		};
		this.$=function(name) {
			var expression=/^\#.*$/;
			var v=expression.test(name);
			if(v==true){
				if(name.indexOf("#")!= -1){
					name= document.getElementById(name.slice(1));
				}
			}else{
				if(name.indexOf(".")!= -1){
					name= document.getElementsByClassName(name.slice(1))[0];
				}
			}
			 return name;
		};
		//判断是否存在class属性方法
		this.hasClass = function(elements, cName) {
			return !!elements.className.match(new RegExp("(\\s|^)" + cName + "(\\s|$)"));
		};
		//添加class属性方法
		this.addClass = function(elements, cName) {
			if(!this.hasClass(elements, cName)) {
				elements.className += " " + cName;
			};
		};
		//删除class属性方法 elements当前结构  cName类名
		this.removeClass = function(elements, cName) {
			if(this.hasClass(elements, cName)) {
				elements.className = elements.className.replace(new RegExp("(\\s|^)" + cName + "(\\s|$)"), " "); // replace方法是替换
			};
		};
		this.eve=function(eve){
			var evt = eve || window.event; //指向触发事件的元素
			var obj = evt.target || evt.srcElement || eve.srcElement; //指向触发事件的元素
			return obj;
		},
		//根据class类名条件筛选结构
		this.getByClass = function(oParent, sClass) { //根据class获取元素
			var oReasult = [];
			var oEle = oParent.getElementsByTagName("*");
			for(i = 0; i < oEle.length; i++) {
				if(oEle[i].className == sClass) {
					oReasult.push(oEle[i]);
				}
			};
			return oReasult;
		};
		this.isArray=function(o) {
		　　return Object.prototype.toString.call(o);
		};
		this.Attributes=function(parent,name,value){
			var Attributes = document.createAttribute(name);
			Attributes.nodeValue =value;
			return parent.setAttributeNode(Attributes);
		};
			//根据class类名条件筛选结构
		this.getElementsByClassName = function(parent, className) {
			//获取所有父节点下的tag元素　
			var aEls = parent.getElementsByTagName("*");　　
			var arr = [];
			//循环所有tag元素　
			for(var i = 0; i < aEls.length; i++) {
				//将tag元素所包含的className集合（这里指一个元素可能包含多个class）拆分成数组,赋值给aClassName	　　　　
				var aClassName = aEls[i].className.split(' ');　　　　 //遍历每个tag元素所包含的每个className
				for(var j = 0; j < aClassName.length; j++) {　　　　　　 //如果符合所选class，添加到arr数组				　　　　　
					if(aClassName[j] == className) {　　　　　　　　
						arr.push(aEls[i]);　　　　　　　　 //如果className里面包含'box' 则跳出循环						　　　　　　　　
						break; //防止一个元素出现多次相同的class被添加多次						　　　　　　
					}　　　　
				};　　
			};　　
			return arr;
		};
		//本地长期数据保存
		this.Storage=function(eve,key,value){
			var ms = "mystorage";
			var storage=window.localStorage;
			var init = function(){
				storage.setItem(ms,'{"data":{}}');
			};
			if(!window.localStorage){
			    alert("浏览器不支持localstorage");
			    return false;
			}
			if(eve=="set"){
			    //存储
			    var mydata = storage.getItem(ms);
			    if(!mydata){
			        init();
			        mydata = storage.getItem(ms);
			    }
			    mydata = JSON.parse(mydata);
			    mydata.data[key] = value;
			    storage.setItem(ms,JSON.stringify(mydata));
			    return mydata.data;
			}else if(eve=="get"){
			    //读取
			    var mydata = storage.getItem(ms);
			    if(!mydata){
			        return false;
			    }
			    mydata = JSON.parse(mydata);
			    return mydata.data[key];			
			}else if(eve=="remove"){
				 //读取
			    var mydata = storage.getItem(ms);
			    if(!mydata){
			        return false;
			    }
			    mydata = JSON.parse(mydata);
			    delete mydata.data[key];
			    storage.setItem(ms,JSON.stringify(mydata));
			    return mydata.data;			
			}else if(key=="shift"){
                var mydata = storage.getItem(ms);
				if(!mydata){
			        return false;
			    }
				mydata = JSON.parse(mydata);
				var datas= mydata.data[key];
				datas.splice(i, 1);
				mydata=mydata;
			    storage.setItem(ms,JSON.stringify(mydata));
			    return mydata.data;
			}		
		};
		//删除指定_element方法
		this.removeElement = function(_element) {
			var _parentElement = _element.parentNode;
			if(_parentElement) {
				_parentElement.removeChild(_element);
			};
		};
		this.par = this.extend(par, options, true);
		this.show(this.par,this);
	},
	show:function(par,obj){
		var doc=obj.$(par.Module);
		var Method=par.Method;
		var size=0;
		window.onresize=function(){
			size++;
			obj.pattern(par,obj,doc,size);
			
		};
		if(document.createEvent) {
            var event = document.createEvent("HTMLEvents");
            event.initEvent("resize", true, true);
            window.dispatchEvent(event);
        } else if(document.createEventObject) {
            window.fireEvent("onresize");
        };
        obj.Methodset(doc,Method,obj);
      
	},
	js:function(par,set){
		var JsFile=set.par.JsFile;
		var newarr=[];
		if(JsFile!=null){
			for(var n = 0; n < JsFile.length; n++) {
				var name=JsFile[n];
				var new_element=document.createElement("script");
				new_element.setAttribute("type","text/javascript");
				new_element.setAttribute("src",name);
				document.head.appendChild(new_element);
			}
		}
	},
	Methodset:function(doc,Method,set){
		var newarr=[];
		if(Method!=null){
			for(var n = 0; n < Method.length; n++) {
		   	    var newgroup = {
					id: n,
					module:Method[n].module,
					event: Method[n].event
				};
				newarr.push(newgroup); //从新整合数组 
			}
			set.Operatemethod(doc,set,newarr);
		}
	},
	Operatemethod:function(doc,set,newarr){
		for(var n= 0; n < newarr.length; n++) {
			var name=newarr[n].module;
			var obj=set.$(name);
			var prompts = document.createAttribute("sys-module");
			prompts.nodeValue ='module'+newarr[n].id;
			obj.setAttributeNode(prompts);
			newarr[n].event(set,obj);//编辑方法
		}
	},
	width: function() {
		return self.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
	},
	height: function() {
		return self.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;

	},
	//判断是手机还是pc
	isMobile: function(mobile_flag) {
		var userAgentInfo = navigator.userAgent;
		var mobileAgents = ["Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"];
		var mobile_flag = false;
		//根据userAgent判断是否是手机
		for(var v = 0; v < mobileAgents.length; v++) {
			if(userAgentInfo.indexOf(mobileAgents[v]) > 0) {
				mobile_flag = true;
				break;
			}
		}
		var screen_width = window.screen.width;
		var screen_height = window.screen.height;
		//根据屏幕分辨率判断是否是手机
		if(screen_width < 500 && screen_height < 800) {
			mobile_flag = true;
		}
		return mobile_flag;
	},
	//声明ajax方法,用于判断浏览器是否支持ajax
	ajaxObject: function(set) {
		var xmlHttp;
		try {
			// Firefox, Opera 8.0+, Safari
			xmlHttp = new XMLHttpRequest();
		} catch(e) {
			// Internet Explorer
			try {
				xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
			} catch(e) {
				try {
					xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
				} catch(e) {
					set.PromptBox('您的浏览器不支持AJAX', 2, false);
					return false;
				}
			}
		}
		return xmlHttp;
	},
	ajaxGet: function(url,reply,prompt,event) {
		//url地址,reply异步或同步,prompt提示信息,event方法
		var set = this;
		var ajax = set.ajaxObject(set);
		ajax.open("GET", url, reply);
		if(ajax) {
			set.PromptBox(prompt, 0, true);
		}
		ajax.onreadystatechange = function(eve) {
			if(ajax.readyState == 4) {
				if(ajax.status == 200) {
					var json = ajax.responseText; //获取到json字符串，还需解析
					var jsonStr = JSON.parse(json); //将字符串转换为json数组
					event(jsonStr);
				} else {
					set.PromptBox("HTTP请求错误！错误码：" + ajax.status, 2);
				}
				set.PromptBox(null, 0, true);
			}
		};
		ajax.send();
	},
	ajaxPost:function(url,reply,prompt,header,event){
		//url地址,reply异步或同步,header请求头,prompt提示信息,event方法
		var set = this;
		var ajax = set.ajaxObject();
		ajax.open("post", url, reply);			
		ajax.setRequestHeader("Content-Type",header);
		if(ajax) {
			set.PromptBox(prompt, 0, true);
		}
		ajax.onreadystatechange = function(eve) {
			if(ajax.readyState == 4) {
				if(ajax.status == 200) {
					var json = ajax.responseText; //获取到json字符串，还需解析
					var jsonStr = JSON.parse(json); //将字符串转换为json数组
					event(jsonStr);
				}else{
					set.PromptBox("HTTP请求错误！错误码：" + ajax.status, 2);
			   }
				set.PromptBox(null, 0, true);
			}
		}
		ajax.send();
	},
	//设置一个提示框，编辑提示框，texts为提示文本 ，time为显示时间秒单位
	PromptBox: function(texts, time, status) {
		var set = this;
		var b = document.body.querySelector(".box_Bullet");
		if(!b) {
			var box = document.createElement("div");
			document.body.appendChild(box).className = "box_Bullet";
			var boxcss = document.querySelector(".box_Bullet");
			var winWidth = window.innerWidth;
			document.body.appendChild(box).innerHTML = texts;
			var wblank = winWidth - boxcss.offsetWidth;
			box.style.cssText = "width:" + boxcss.offsetWidth + "px" + "; left:" + (wblank / 2) + "px" + ";" +"margin-top:" + (-boxcss.offsetHeight / 2) + "px";
			var int = setInterval(function() {
				time--;
				set.endclearInterval(time, box, int);
			}, 1000);

		} else if(status == true) {
			document.body.removeChild(b);
			return;
		}
	},
	endclearInterval: function(time, box, int) {
		time > 0 ? time-- : clearInterval(int);
		if(time == 0) {
			clearInterval(int);
			document.body.removeChild(box);
			return;
		}
	},
	//添加Cookie
	setCookie:function(name,value,obj,cookieDate){
		var config=function(){
			type:cookieDate
		};
		if(obj){
	        Object.assign(config(),obj);
		}
		var oDate = new Date();
		var expires = null;
		if(config.expires){
	        if(config.type){
	            switch (config.type){
	                case '秒':
	                    oDate.setSeconds(oDate.getSeconds() + config.expires)
	                    expires = oDate.toUTCString();
	                break;
	                case '分':
	                    oDate.setMinutes(oDate.getMinutes() + config.expires)
	                    expires = oDate.toUTCString();
	                break;
	                case '时':
	                    oDate.setHours(oDate.getHours() + config.expires)
	                    expires = oDate.toUTCString();
	                break;
	                case '天':
	                    oDate.setDate(oDate.getDate() + config.expires);
	                    expires = oDate.toUTCString();
	                break;
	                case '月':
	                    oDate.setMonth(oDate.getMonth() + config.expires);
	                    expires = oDate.toUTCString();
	                break;
	                case '年':
	                    oDate.setFullYear(oDate.getFullYear() + config.expires);
	                    expires = oDate.toUTCString();
	                break;
	                default:
	                    oDate.setDate(oDate.getDate() + config.expires);
	                    expires = oDate.toUTCString();
	                break;
	            }
	        }else{
	            oDate.setDate(oDate.getDate() + config.expires);
	            expires = oDate.toUTCString();
	        }
	    }else{
	        oDate.setDate(oDate.getDate() + 1);
	        expires = oDate.toUTCString();
	    }
	    document.cookie = name + "=" + encodeURIComponent(value) + ";expires=" + expires;
	},
	getCookie:function(key){
		//获取cookie
		var str = document.cookie.replace(/;\s*/,';');
        var cookieArr = str.split(';');
        var cookieObj = {};
        var len = cookieArr.length;
        for(var i = 0; i < len;i++){
            var data = cookieArr[i];
            var k = data.split('=')[0];
            var v = data.split('=')[1];
            cookieObj[k] = v;
        }
        if(cookieObj[key]){
            return decodeURIComponent(cookieObj[key]);
        }else{
            return false;
        }
	},
	removeCookie:function(key){
		//删除cookie
		document.cookie = key + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
	},
	//模块层点击方法合集
	LayerModule:function(set,Method,Sysframe){
		document.onclick=function(event){	
			var obj=set.eve(event); 
			var operate=obj.getAttribute('sys-Layer');
		    if(operate=="sys-Layer-fullscreen"){ 
				set.fullscreen(set,obj,Sysframe);
			}else if(operate=="sys-Layer-operate"){
				
				   
			};
		}
	},
	//全屏模式不支持IE8
	fullscreen:function(set,obj,Sysframe){
		var eventname=obj.getAttribute('sys-event');
		var requestFullScreen=function(obj) {
			set.Attributes( obj,"sys-event","exitFullscreen");
			obj.children[0].innerHTML=Sysframe.iconShrink;
			var de = document.documentElement;
			if(de.requestFullscreen) {
				de.requestFullscreen();
			} else if(de.mozRequestFullScreen) {
				de.mozRequestFullScreen();
			} else if(de.webkitRequestFullScreen) {
				de.webkitRequestFullScreen();
			}
		}
		var exitFullscreen=function(obj) {
			set.Attributes( obj,"sys-event","requestFullScreen");
			obj.children[0].innerHTML=Sysframe.iconExpandicon;
			var de = document;
			if(de.exitFullscreen) {
				de.exitFullscreen();
			} else if(de.mozCancelFullScreen) {
				de.mozCancelFullScreen();
			} else if(de.webkitCancelFullScreen) {
				de.webkitCancelFullScreen();
			}
		}
		if(eventname=="requestFullScreen"){
			requestFullScreen(obj);
		}
		if(eventname=="exitFullscreen"){
			exitFullscreen(obj);
		}
	},
	//设置框架基本样式
	pattern: function(par,set,doc,size) {
		var Sysframe=par.SYSframe;//框架
		if(Sysframe!=null){
	        var hrederheight=Sysframe.hrederheight, //顶部高度
			    footerheight=Sysframe.footerheight, //底部高度
				menuWidth=Sysframe.menuWidth,//伸缩栏宽度
				header=set.$(Sysframe.header), //框架顶部
				footer=set.$(Sysframe.footer), //框架底部
				content=set.$(Sysframe.content), //框架内容区
				tabs=set.$(Sysframe.tabs),//选项卡切换
				closebtn=set.$(Sysframe.closebtn), //点击隐藏
				showbtn=set.$(Sysframe.showbtn), //点击显示	
				slide=set.$(Sysframe.slide),//滑动区域
				iframe=set.$(Sysframe.iframe),//显示区域
				tabposition=tabposition,//栏目选项卡位置
				localStorage=Sysframe.localStorage,
				messageform=set.$(Sysframe.message),//窗体内容显示区域
				menuModule=set.$(Sysframe.menuModule),//菜单模块
				scrollarea=set.$(Sysframe.scrollarea),//滚动区域
				sessionArray=par.sessionArray,//[]
				tabclickname=set.$(Sysframe.tabclickname);//指定点击选项卡切换栏目的事件元素
				var mobile_flag =set.isMobile(mobile_flag);	
				doc.style.width=set.width()+"px";
				doc.style.height=set.height()+"px";
			if(mobile_flag){
				set.addClass(doc,"SYS-mobileStyle");
				set.removeClass(doc,"SYS-PCStyle");
				/*当为移动端时执行 设计图文档宽度 */
				var docWidth = 760;
				var doc = window.document,
					docEl = doc.documentElement,
					resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize';
				var recalc = (function refreshRem() {
					var clientWidth = docEl.getBoundingClientRect().width;
					/* 8.55：小于320px不再缩小，11.2：大于420px不再放大 */
					docEl.style.fontSize = Math.max(Math.min(20 * (clientWidth / docWidth), 11.2), 8.55) * 5 + 'px';
					return refreshRem;
				})();
				/* 添加倍屏标识，安卓倍屏为1 */
				docEl.setAttribute('data-dpr', window.navigator.appVersion.match(/iphone/gi) ? window.devicePixelRatio : 1);
	
				if(/iP(hone|od|ad)/.test(window.navigator.userAgent)) {
					/* 添加IOS标识 */
					doc.documentElement.classList.add('ios');
					/* IOS8以上给html添加hairline样式，以便特殊处理 */
					if(parseInt(window.navigator.appVersion.match(/OS (\d+)_(\d+)_?(\d+)?/)[1], 10) >= 8)
						doc.documentElement.classList.add('hairline');
				}
				if(!doc.addEventListener) return;
				window.addEventListener(resizeEvt, recalc, false);
				doc.addEventListener('DOMContentLoaded', recalc, false);
			}else{
				set.removeClass(doc,"SYS-mobileStyle");
				set.addClass(doc,'SYS-PCStyle');
			}
			var mw =null, pw =null;
			header.style.cssText="height:"+hrederheight+"px;position:relative;z-index:1;"+"line-Height:"+hrederheight+"px";//顶部样式
			footer.style.cssText="height:"+footerheight+"px;position:relative;z-index:1;"+"line-Height:"+footerheight+"px";//底部样式
			var ul = document.createElement("ul");
			set.addClass(ul,'tablist');
			tabs.style.height=footerheight+'px';
			var direction=set.getElementsByClassName(footer,"sys-direction");
			var dw=0;
			direction.myMap(function(eve){dw+=eve.offsetWidth});
			tabs.style.width=(footer.offsetWidth-dw-menuWidth)+'px';
			size==1?tabs.appendChild(ul):'';
			var contenth=set.height()-hrederheight;
			var iframeh=set.height()-hrederheight-footerheight;
			content.style.cssText="height:"+contenth+"px;position:relative;;z-index:0;"+"width:"+set.width()+"px";//底部样式
			iframe.style.cssText="height:"+iframeh+"px;position:relative;;z-index:0;"+"width:100%";//底部样式
			if(menuModule!=null){
				menuModule.style.width != "" ? mw = parseInt(menuModule.style.width.replace("px", "")) : '';
				if(mw == 0) {
					set.addClass(menuModule,"leftMessage");
					menuModule.style.cssText="left: 0;position:relative;z-index:1; float:left; width:"+ set.width()+"px";
				} else {
					menuModule.style.cssText="width:"+menuWidth+"px;left:0;position:relative;z-index:2; float:left; height:"+contenth+"px";
					set.addClass(menuModule,"leftModule");
					set.addClass(messageform,"rightMessage");
					messageform.style.cssText="float:left; left:"+menuWidth+"px; height:"+contenth+"px;width:"+(set.width()-menuWidth)+"px";
				}	
			}
			var c = document.createAttribute("sys-event");
			c.nodeValue ='close';
			closebtn.setAttributeNode(c);
			var s = document.createAttribute("sys-event");
			s.nodeValue ='show';
			showbtn.setAttributeNode(s);
			set.menubtnhover(set,slide,menuWidth,closebtn,showbtn,menuModule,messageform,Sysframe,sessionArray);
		}
	},
	//滚动条方法
	scrollbar:function(set,wrapDiv,contentDiv){
		var sliderWrap = document.createElement("div");
		set.addClass(sliderWrap,'sliderWrap' );
		sliderWrap.id="sliderWrap";
		wrapDiv.appendChild(sliderWrap);
		var slider = document.createElement("div");
		set.addClass(slider,'slider' );
		slider.id="slider";
		sliderWrap.appendChild(slider);
		//设置比例
		//clientHeight - 不包括border
		var scale = wrapDiv.clientHeight / contentDiv.clientHeight;
		//设置滑块的高度
		var h1 = sliderWrap.clientHeight * scale;
		//为了合理设置高度，设置滑块的最小高度
		if (h1 < 50) { 
			h1 = 50;
		}else if (scale >= 1) {
			//说明当前内容能过完全显示在可视区域内，不需要滚动条
			sliderWrap.style.display = "none";
		}else{
			sliderWrap.style.display = "none";
		}
		//设置滑块的高度
		slider.style.height = h1 +"px";
		//设置y轴的增量
		var y = 0;
		//为wrap添加滚轮事件
		wrapDiv.onmousewheel = function(e){
			var event1 = event || e;
			var conh=contentDiv.clientHeight;
			var wraph=wrapDiv.clientHeight;
			var scale = wraph / conh;
		    //设置滑块的高度
		   var h1 = sliderWrap.clientHeight * scale;
		   //设置滑块的高度
		   slider.style.height = h1 +"px";
			if(conh>wraph){
				sliderWrap.style.display = "block";
			}else{
				sliderWrap.style.display = "none";
			}
			if (event.wheelDelta < 0) {
				//滑动条向下滚动
				y += 10;
			}else if (event.wheelDelta > 0) {
				//滑动条向上滚动
				y -= 10;
			}
			//y变化时说明在滚动，此时使滚动条发生滚动，以及设置content内容部分滚动
			//判断极端情况，滑块不能划出屏幕
			if (y <= 0) {
				//滑块最多滑到顶部
				y = 0;
			}
			if(y >= sliderWrap.clientHeight - slider.clientHeight){
				//滑块最多滑到最底部
				y = sliderWrap.clientHeight - slider.clientHeight;
			}
			//更新滑块的位置
			slider.style.top = y +"px";
			scale = wrapDiv.clientHeight / contentDiv.clientHeight;
			contentDiv.style.top = - y / scale +"px";
		}	
	},
	//左侧菜单栏等鼠标移入移除点击等鼠标事件方法集合
	menubtnhover: function(set,slide,menuWidth,closebtn,showbtn,menuModule,messageform,Sysframe,sessionArray) {
		var mouIconClose=set.par.mouIconClose; //箭头
		var mouIconOpen=set.par.mouIconOpen;// 箭头
		var hover=function(obj,name){
			var w = slide.offsetWidth;
			if(w >= menuWidth) {
				if(name=="mouseove"){
					set.addClass(obj,"display_btn");
				    closebtn.style.cssText="display:block";
				}else if(name=="mouseout"){
					set.removeClass(obj,"display_btn");
				    closebtn.style.cssText="display:none";
				}
			} else {
				set.removeClass(obj,"display_btn");
				closebtn.style.cssText="display:none";
			}
		}
		$$(menuModule).over(function(eve){
			var sliderWrap= set.$("#sliderWrap");
			var obj=eve.obj;
			hover(obj,"mouseove");
			sliderWrap.style.display = "block";			
		}).out(function(eve){
			var sliderWrap= set.$("#sliderWrap");
			var obj=eve.obj;
			hover(obj,"mouseout");
			sliderWrap.style.display = "none";
		});
		var Clicks=0;
		//菜单栏点击事件处理事件方法
		document.onclick=function(eve){
			var obj=set.eve(obj);
			var frame=set.$(set.par.Module);
			var clickmenu=set.hasClass(obj,Sysframe.clickmenu);
			var clicktab=set.hasClass(obj.parentNode,"tabbar-name");
			var CloseSection=set.hasClass(obj,"close_section");
			var sortmodeWidth=Sysframe.sortmodeWidth;
			var type=obj.getAttribute('sys-event');
			var position=obj.getAttribute('data-position');
			var method=obj.getAttribute("sys-method");//选项卡操作
			var messagew = messageform.offsetWidth;
			var foot=set.$(Sysframe.footer);
			var tabs=set.$(Sysframe.tabs);//选项卡切换
			var direction=set.getElementsByClassName(foot,"sys-direction");
			var dw=0;
			var reg = /\d+/g;
			var messagew=messageform.offsetWidth;
			direction.myMap(function(eve){dw+=eve.offsetWidth});
			if(type!=null){
				if(type=="close"){
					var timesRun =menuWidth;
					var wi=0;
					var interval = setInterval(function(){
					    timesRun -=5;
					    wi++; 
					    menuModule.style.cssText+="width:"+timesRun+"px";
						messageform.style.cssText+="left:"+timesRun+"px;width:"+(messagew+menuWidth)+"px";
					    timesRun > 0 ? timesRun-- : clearInterval(interval);
	                },1);
	                tabs.style.width=(tabs.offsetWidth+menuWidth)+'px';
	                var munlist= set.getElementsByClassName(menuModule,'bk-section-module');
					munlist.myMap(function(eve){
						eve.style.cssText="display:none";
					});
					closebtn.style.cssText="display:none";
					showbtn.style.cssText="display:block";					
				}else if(type=="show"){
					var timesRun = 0;
					var interval = setInterval(function(){
						timesRun+=5;
						menuModule.style.cssText+="width:"+timesRun+"px";
						messageform.style.cssText+="left:"+timesRun+"px;width:"+(messagew-menuWidth)+"px";
						if(timesRun ===menuWidth){    
					        clearInterval(interval);    
					    }
					},1)
					tabs.style.width=(tabs.offsetWidth-menuWidth)+'px';
					var munlist= set.getElementsByClassName(menuModule,'bk-section-module');
					munlist.myMap(function(eve){
						eve.style.cssText="display:block";
					});
		            showbtn.style.cssText="display:none";
		            closebtn.style.cssText="display:block";
				}else if(type=="iconmode"){
					var p= obj.parentNode;
					var iconlist= set.getElementsByClassName(p,'sys-region');
					var arrow= set.getElementsByClassName(menuModule,'arrow');
					for(var i=0; i<arrow.length;i++){
						 arrow[i].innerHTML='&#xe649;'
					}
					var timesRun = menuWidth;
					var interval = setInterval(function(){
						timesRun -=5;
						messageform.style.cssText+="left:"+timesRun+"px;width:"+(messagew+menuWidth-timesRun)+"px";
						menuModule.style.width=timesRun+'px';
						if(timesRun ===sortmodeWidth){  
							obj.style.cssText="display:none";
							iconlist[1].style.cssText="display:block";
					        clearInterval(interval);
					   }
					},1);
					tabs.style.width=(tabs.offsetWidth+menuWidth-sortmodeWidth)+'px';
				    set.addClass(menuModule,'bk-icon-menu');
					menuModule.style.cssText+="font-size:0px";	
				}else if(type=="textmode"){
					var p= obj.parentNode;
					var iconlist= set.getElementsByClassName(p,'sys-region');
					var timesRun = sortmodeWidth;
					var interval = setInterval(function(){
						timesRun +=5;
						menuModule.style.width=timesRun+'px';
						messageform.style.cssText+="left:"+timesRun+"px;width:"+(messagew-menuWidth+sortmodeWidth)+"px";
						if(timesRun ===menuWidth){  
							obj.style.cssText="display:none";
							iconlist[0].style.cssText="display:block";
					        clearInterval(interval);     
					   }
					},1);
					tabs.style.width=(tabs.offsetWidth-menuWidth+sortmodeWidth)+'px';
					set.removeClass(menuModule,'bk-icon-menu');
					menuModule.style.fontSize="";
					var arrow= set.getElementsByClassName(menuModule,'arrow');
					for(var i=0; i<arrow.length;i++){
						 arrow[i].innerHTML=mouIconOpen
					}					
				}else if(type=="slide"){
					var direction=obj.getAttribute("sys-direction");
					var tabslist=set.getElementsByClassName(tabs,"tabbar-name");
					var mw=tabs.offsetWidth;
					var ul=set.getElementsByClassName(tabs,"tablist")[0];
					var tw=0;
					var last=0;
					tabslist.myMap(function(eve,i){
						tw +=eve.offsetWidth; 
						if(tabslist.length==i+1){
							last=eve.offsetWidth+20;
						}
					});
					if(mw<tw){
						var ml=ul.style.marginLeft;
						var lpx=ml.match(reg);
						lpx!=null?lpx=parseInt(lpx[0]):'';
						if(direction=="left"){
							var leftw=(lpx+last);
						    ul.style.cssText="margin-left:-"+leftw+"px";
						}else if(direction=="right"){
							var leftw=(lpx-last);
							ul.style.cssText="margin-left:-"+leftw+"px";
						}
					}
					Clicks++;
				}
			}
			set.Sectionmethod(set,obj,clickmenu);
			//
			if(clickmenu){
				var parameter="menu";//编辑判断方法
			    set.PageEvent(set,obj,Sysframe,parameter,sessionArray,null);
			}
			//选项卡切换方法
			if(clicktab){
				var parameter="tabs";//编辑判断方法
				set.PageEvent(set,obj,Sysframe,parameter,sessionArray,null);
			}
			//关闭选项卡栏目以及其信息方法
			if(CloseSection){
				var parameter="Closetabs";//编辑判断方法
				var last=obj.parentNode.parentNode.offsetWidth;
				set.Closemethod(set,obj,Sysframe,parameter);
				var tabslist=set.getElementsByClassName(tabs,"tabbar-name");
				var ul=set.getElementsByClassName(tabs,"tablist")[0];
				var mw=tabs.offsetWidth;
				var tw=0;
				
				tabslist.myMap(function(eve,i){
						tw +=eve.offsetWidth; 
				});
				var margin=(tw-mw);
				if(margin<=0){
					var ml=ul.style.marginLeft;
					var lpx=ml.match(reg);
					lpx!=null?lpx=parseInt(lpx[0]):'';
					var leftw=(lpx-last);
					ul.style.cssText="margin-left:-"+leftw+"px";
				}
			}
			//刷新选项卡操作
			if(method=="tabReload"){
				
				set.refreshTab(set,obj,Sysframe,tabs);
			}
			//关闭当前打开的选项卡操作
			if(method=="tabCloseCurrent"){
				set.tabCloseCurrent(set,obj,Sysframe,tabs);
			}
			//关闭全部选项卡
			if(method=="tabCloseAll"){
				set.tabCloseAll(set,obj,Sysframe,tabs);
			} 
			//关闭当前以外的选项卡
			if(method=="tabCloseOther"){
				set.tabCloseOther(set,obj,Sysframe,tabs);
			}
			//全屏模式方法
			var operate=obj.getAttribute('sys-Layer');
		    if(operate=="sys-Layer-fullscreen"){ 
				set.fullscreen(set,obj,Sysframe);
			}
			var f=obj.parentNode.children;
			var selected=set.hasClass(obj,'selected');
			var arrowfunction=function(arrow,icon){
				for(var i=0;i<arrow.length;i++){
					var name=set.hasClass(arrow[i],'icon-arrow');
					if(name){
					 	arrow[i].innerHTML=icon;
					}
				}
			}
			var bombbox=function(name,display){
				for(var i=0;i<name.length;i++){
					var b=set.hasClass(name[i],'Bombbox');
					if(b){
						var u=name[i];
					    var timescut = u.offsetHeight;
					    var bh=u.getAttribute('sys-Bombbox-height');
						var interval = setInterval(function(){
							timescut-=10;
							u.style.height = timescut+'px';
							if(timescut <=0){
								u.style.display =display;
								u.style.overflow ='hidden';
								if(display=="block"){
									set.addClass(u,'reveal');
									if(bh){
										u.style.height=bh;
									}else{
										u.style.height='';
									}
								}else{
									set.removeClass(u,'reveal');
									u.style.overflow='';
									u.style.height='';
								}
						        clearInterval(interval);    
						    }
						},1);
				    }
			    }	
			}
			var clkbox=function(selected,obj,f){
				if(selected){
					set.removeClass(obj,'selected');
					var arrow=obj.children;  
					arrowfunction(arrow,mouIconClose);
					bombbox(f,'none');
				}else{
					set.addClass(obj,'selected');
					var arrow=obj.children;
					arrowfunction(arrow,mouIconOpen);
					bombbox(f,'block');
				}	
			}
			if(position=="Bombbox"){
				var list=set.getElementsByClassName(frame,'Bombbox');
				for(var l=0;l<list.length;l++){
					var box=set.hasClass(list[l],"reveal");
					if(box){
						var name=list[l].parentNode.children;
						for(var c=0;c<name.length;c++){
							var a=set.hasClass(name[c],'selected');
							if(a){
								clkbox(a,name[c],name);
							}
						}
					}else{
						clkbox(selected,obj,f);
					}
				}
			}else{
				var list=set.getElementsByClassName(frame,'Bombbox');
				for(var l=0;l<list.length;l++){
					var box=set.hasClass(list[l],"reveal");
					if(box){
						var name=list[l].parentNode.children;
						for(var c=0;c<name.length;c++){
							var a=set.hasClass(name[c],'selected');
							if(a){
								clkbox(a,name[c],name);
							}
						}
					}
				}
			}
		}
	},
	
	//刷新选项卡操作方法
	refreshTab: function(set,obj,Sysframe,show_nav) {
		var menu = JSON.parse(window.sessionStorage.getItem(Sysframe.sessionname));
		var navLi=set.getElementsByClassName(show_nav,"tabbar-name");
		var sessionArray=set.par.sessionArray;
		var iframe_box = set.$(Sysframe.iframe);
		var Refresh="refresh";
		var ajaxhtml = function(eve,title){
		   var pageModule=set.getElementsByClassName(iframe_box,"show_iframe");
		   pageModule.myMap(function(eve,i){
		   	    var ftitle=eve.getAttribute('title');
		   	    if(ftitle==title){
		   	        	set.removeElement(eve);
		   	    }
		   })
		}
		navLi.myMap(function(eve,i){
			if(set.hasClass(eve,"active")){
				var mode=eve.getAttribute("sys-mode");
				var title=eve.getAttribute("title");
				if(mode=="html"){
					ajaxhtml(eve,title);
					set.PageEvent(set,eve,Sysframe,Refresh,sessionArray,menu);
				}else if(mode=="iframe"){
					ajaxhtml(eve,title);
					set.PageEvent(set,eve,Sysframe,Refresh,sessionArray,menu);
				}
			}
		})
	},
	//关闭全部选项卡方法
	tabCloseAll: function(set,obj,Sysframe,show_nav) {
		var navLi=set.getElementsByClassName(show_nav,"tabbar-name");
		var menu = JSON.parse(window.sessionStorage.getItem(Sysframe.sessionname));
		navLi.myMap(function(eve,i){
			if(set.hasClass(eve,"disabled")){
				event.preventDefault();
			}else{
				var id=eve.getAttribute('sys-id');
				if(id!=Sysframe.defaultid){
					var parameter="Closetabs";//编辑判断方法
					var tabClose=true;
					for(var i = 0; i < menu.length; i++) {
						if(menu[i].id!=Sysframe.defaultid) {
							set.Closemethod(set,eve,Sysframe,parameter,tabClose);
						}
					}
				}else{
					set.PromptBox('当前页不能关闭！', 2);
				}
			}
		})
	},
	//除打开的页面之外全部关闭方法
	tabCloseOther: function(set,obj,Sysframe,show_nav) {
		var navLi=set.getElementsByClassName(show_nav,"tabbar-name");	
		navLi.myMap(function(eve,i){
			var id=eve.getAttribute('sys-id');
			if(!set.hasClass(eve,"active")){
				if(id!=Sysframe.defaultid){
					var parameter="Closetabs";//编辑判断方法
					var tabClose=true;
					set.Closemethod(set,eve,Sysframe,parameter,tabClose);	
				}
			}
		})	
	},
	//关闭当前页的方法
	tabCloseCurrent:function(set,obj,Sysframe,show_nav){
		var navLi=set.getElementsByClassName(show_nav,"tabbar-name");
		navLi.myMap(function(eve,i){
			if(set.hasClass(eve,"active")){
				var id=eve.getAttribute('sys-id');
				if(id==Sysframe.defaultid){
					set.PromptBox('当前页不能关闭！', 2);
				}else{
					var parameter="Closetabs";//编辑判断方法
					var tabClose=true;
				    set.Closemethod(set,eve,Sysframe,parameter,tabClose);
				}
			}
		})
	},
	//选项卡处理事件方法删除方法
	Closemethod:function(set,obj,Sysframe,parameter,Close){
		var show_nav = set.$(Sysframe.tabs);
		if(Close==true){
			var section=obj;	
		}else{
		   var section=obj.parentNode;	
		}
		var li=section.parentNode;
		var title=section.getAttribute("title");//标题名称
		var url=section.getAttribute("sys-href");//地址链接
		var id=section.getAttribute("sys-id");//ID
		var Storage = function(e) {
			var menu = JSON.parse(window.sessionStorage.getItem(Sysframe.sessionname));
			if(menu!=null){	
				for(var i = 0; i < menu.length; i++) {
					if(e.getAttribute('title') == menu[i].title) {
						menu.splice(i, 1);
						window.sessionStorage.setItem(Sysframe.sessionname, JSON.stringify(menu));
					}
				}
			}
		}
		var pageModule=set.getElementsByClassName(iframe_box,"show_iframe");
		var show_navLi =set.getElementsByClassName(show_nav,"tabbar-name");
		var f=null;
		for(var p=0;p<pageModule.length;p++){
			var show_iframe=pageModule[p];
			var iframename=show_iframe.getAttribute("title");
			var iframeid=show_iframe.getAttribute("sys-id");
			if(iframename==title || iframeid==id){
				p==0?f=0:f=p-1;
				var n=null;
				for(var s=0; s<show_navLi.length;s++){
					var navid=show_navLi[s].getAttribute("sys-id");
					if(navid==iframeid){
						var has=set.hasClass(show_navLi[s],'active');
						has?n=s-1:'';
					}
		        }
				Close==true?set.removeElement(section):set.removeElement(section.parentNode);
				set.removeElement(show_iframe);
				Storage(section);
				if(n!=null){
					var mu=show_navLi[n];
				    set.addClass(mu,'active');
				    set.Attributes( mu,"sys-status",1);
				    set.menusess(set,Sysframe,mu,parameter);   
				}      
			}else{}
		}
		if(f!=null){
			var iframe=pageModule[f];
			var active=set.hasClass(show_navLi[f],"active")
			if(active){
				iframe.style.display="block";
			    set.addClass(iframe,'selected');
			}else{}
		}
	},
	//菜单栏目展开隐藏方法
	Sectionmethod:function(set,obj,clickmenu){
	    var Sysframe=set.par.SYSframe;//框架
	    var menuModule=set.$(Sysframe.menuModule);//菜单模块
	    var mouIconClose=set.par.mouIconClose; //箭头
		var mouIconOpen=set.par.mouIconOpen;// 箭头
    	if(clickmenu){
	    	var mun= obj.parentNode.children;
			var menulay=set.getElementsByClassName(menuModule,"sys-level");
			var column=set.getElementsByClassName(menuModule,"sys-column");
			var closedmethod=function(u,obj){
				var arrow=obj.children;
				if(u!=null){
					var timescut = u.offsetHeight;
				    var interval = setInterval(function(){
					timescut-=10;
					u.style.height = timescut+'px';
					u.style.overflow = "hidden";
					if(timescut <=0){
						u.style.display = "none";
						u.style.height="";
				        clearInterval(interval);    
				    }
				},1);
				}
				set.removeClass(obj.parentNode,"open");
				set.Attributes(obj.parentNode,"ischek",false);
				for(var a=0;a<arrow.length;a++){
					if(set.hasClass(arrow[a],'arrow')){
    	 			if(set.hasClass(menuModule,'bk-icon-menu')){
						 	arrow[a].innerHTML='&#xe649;'
						 }else{
						 	arrow[a].innerHTML=mouIconOpen
						 }
    	 		    }
				}
			};
			var showmethod=function(u,obj){
				var arrow=obj.children;
				if(u!=null){
					var timesRun =0;
					u.style.display = "block";
//					var h=u.offsetHeight;
//					var intervals = setInterval(function(){
//						timesRun ++;
//						u.style.height = timesRun+'px';
//						if(timesRun ==h){
//							clearInterval(intervals);
//							u.style.height="";  
//					    }
//					},1);
				}
				set.addClass(obj.parentNode,"open");
				set.Attributes( obj.parentNode,"ischek",true);	
				for(var a=0;a<arrow.length;a++){
					if(set.hasClass(arrow[a],'arrow')){
						if(set.hasClass(menuModule,'bk-icon-menu')){
						 	arrow[a].innerHTML='&#xe649;'
						}else{
						 	arrow[a].innerHTML=mouIconClose
						}
    	 		    }
				}
			    return false;
			};
			var child=set.getElementsByClassName(obj.parentNode,"sys-childmenu");
			if(child.length==0){
				var getischek=obj.parentNode.getAttribute("ischek");
					for(var m=0;m<column.length;m++){
						var open=set.hasClass(column[m],'open');
						if(open){
							var Father=set.getElementsByClassName(column[m],"sys-childmenu");
							if(Father.length==0){
							closedmethod(null,column[m].children[0]);
							}else{
								var mobj=Father[0].parentNode.children[0];
								closedmethod(Father[0],mobj);
							}
						}
					}
				if(getischek=='false'){
					showmethod(null,obj);
				}
			}else{
				for(var i=0;i<mun.length;i++){
					var u=mun[i];
					var childmenu=set.hasClass(mun[i],"sys-childmenu");
					if(childmenu){
						var getischek=obj.parentNode.getAttribute("ischek");
						for(var m=0;m<menulay.length;m++){
							var open=set.hasClass(menulay[m],'open');
							if(open){
								var mChild=set.getElementsByClassName(menulay[m],"sys-childmenu");
								var mobj=mChild[0].parentNode.children[0];
								closedmethod(mChild[0],mobj);
							}
						}
						if(getischek=='false'){ 
							showmethod(u,obj);
						}
					}else{
					}
				}	
			}
    	}else{
			return false
		}
	},	
	//默认加载显示页
	defaultFrame:function(set,menuModule){
		var Refresh="refresh";
		var Sysframe=set.par.SYSframe;
		var defaultid=Sysframe.defaultid;
		var title=Sysframe.defaultpage;	
		var menu = JSON.parse(window.sessionStorage.getItem(Sysframe.sessionname));
		var getStorage=set.Storage("get",Sysframe.localStorage);
		var Storage=function(sessionArray,json){
			set.tabshtml(set,json,Sysframe);
			var show_nav = set.$(Sysframe.tabs);
			var show_navLi =set.getElementsByClassName(show_nav,"tabbar-name");
			for(var i=0;i<show_navLi.length;i++){
				var id=show_navLi[i].getAttribute("sys-id");//ID
				var url=show_navLi[i].getAttribute("sys-href");//地址链接	
				for(var u = 0; u < json.length; u++) {	
					var sid=json[u].id;//ID
				    var surl=json[u].url//地址链接
					if(sid==id || surl==url ){
						set.PageEvent(set,show_navLi[i].children[0],Sysframe,Refresh,sessionArray,json[u]);
					}
				}
			}
			set.positionFrame(set,Sysframe);
		}
		if(menu!=null){
			var sessionArray=menu;
			Storage(sessionArray,menu);
		}else{
			var sessionArray=set.par.sessionArray;
			var column=set.getElementsByClassName(menuModule,"sys-column");
			for(var i=0;i<column.length;i++){
			    var m=column[i].children[0];
				var id=m.getAttribute("sys-id");//ID	
				var href=m.getAttribute("sys-href");//href	
				if(defaultid==id){
					if(href!=""){
						set.PageEvent(set,m,Sysframe,Refresh,sessionArray,menu);
					}else{
						var data=[getStorage];
						Storage(sessionArray,data);
					}	
			   }  
		    }
		}
	},
	positionFrame:function(set,Sysframe){
		
		
	},
	PageEvent:function(set,obj,Sysframe,parameter,sessionArray,menu){
		var newsize = []; //声明一个数组
	    var bStopIndex= 1;
		var bStop = false;
		var url=obj.getAttribute("sys-href");
		if(url!="" && url!=null){
			set.menusess(set,Sysframe,obj,parameter);
			var pageid=obj.getAttribute("sys-id");//ID
			var title=obj.getAttribute("title");//标题名称
			var url=obj.getAttribute("sys-href");//地址链接
			var win=obj.getAttribute("sys-window");//是否启用窗口模式
			var formsize=obj.getAttribute("sys-windowsize");//窗口大小
			var level=obj.getAttribute("sys-level");//等级
			var direction=obj.getAttribute("sys-direction");//方向
			var page=obj.getAttribute("sys-page");//子父级窗口调用的参数
			var pagemode=Sysframe.pagemode,
			    defaultid=Sysframe.defaultid;
			if(Sysframe.sessionname!=null){
				var menulist = JSON.parse(window.sessionStorage.getItem(Sysframe.sessionname));
				if(menulist != null) {
					sessionArray=menulist;
					if(menu!=null){
						if(menu.nate==1){
							bStopIndex = menu.nate;
						}else{
							bStopIndex = menu.nate;
						}
					}
				}
			}
			if(formsize != undefined) {
				var result = formsize.split(",");
				var newgroup = {
					w: result[0],
					h: result[1]
				};
				newsize.push(newgroup); //从新整合数组 
			} else {
				newsize = null;
			}
			if(pagemode == 'html') {
				if(win == "true") {				
					set.par.windowmethod(set,pageid,title,url,bStopIndex,direction,page,bStop,newsize,Sysframe,obj);
				}else{
					set.ajaxhtml(set,pageid,title,url,bStopIndex,direction,page,bStop,Sysframe,obj,sessionArray,parameter);
				}				
			}else if(pagemode == 'iframe'){
				if(win == "true") {				
					set.par.windowmethod(set,pageid,title,url,bStopIndex,direction,page,bStop,newsize,Sysframe,obj);	
				}else{
					if(pageid==defaultid){
						set.ajaxhtml(set,pageid,title,url,bStopIndex,direction,page,bStop,Sysframe,obj,sessionArray,parameter);
					}else{
						set.iframemethod(set,pageid,title,url,bStopIndex,direction,page,bStop,Sysframe,obj,sessionArray,parameter);
					}
				}				
			}
		}else{
	
		}
	},
	//修改数据结构
	menusess:function(set,Sysframe,obj,parameter){
		var menu = JSON.parse(window.sessionStorage.getItem(Sysframe.sessionname));
		var session=function(){
			for(var i = 0; i < amount; i++) {
				if(obj.getAttribute('title') == menu[i].title) {
					menu[i].nate == 0 ? menu[i].nate = 1 :"";
				}else{
					menu[i].nate = 0;
				}		
			}
		}
		if(menu != null) {
		    var amount=menu.length;
		    if(parameter=="Closetabs"){
		    	session();
			}else if(parameter!="refresh"){
				session();
			}
			window.sessionStorage.setItem(Sysframe.sessionname, JSON.stringify(menu));
		}
	},
	//记录保存当前打开的页面(存储到本地json格式数据)
	sessionArr: function(content, titleName, status, pageid, href, sessionArray,Sysframe) {
		var menu = JSON.parse(window.sessionStorage.getItem(Sysframe.sessionname));		
		var json=function(content,titleName,status,pageid,href){
			var group = {
				content: content,
				title: titleName,
				nate: status,
				id: pageid,
				url: href
			}; //编辑数组
			sessionArray.push(group); //添加数组
			var jsonStr = JSON.stringify(sessionArray); //json数组转换为JSON字符串
			window.sessionStorage.setItem(Sysframe.sessionname, jsonStr); //声明存储到名称为session的json中
		}
		if(menu != null) {
			sessionArray=menu;
			var Array = sessionArray.some(function(ele, index, array){
			    if(ele.title == titleName) {
			        return true;
			    }else {
			        return false;
			    }
		    });
		    if(!Array){
		    	json(content,titleName,status,pageid,href);
		    }
		} else {
			json(content,titleName,status,pageid,href);
			
		}
	},
	//界面ajax调用方法
	ajaxhtml:function(set,id,title,_href,bStopIndex,direction,page,bStop,Sysframe,obj,sessionArray,parameter){
		var ajax = set.ajaxObject(set);
		if(page=="false"){
			   var show_nav = set.$(Sysframe.tabs);
			   var show_navLi =set.getElementsByClassName(show_nav,"sys-tabbar");
		}else{
			
		}
		ajax.onreadystatechange = function() {
			if(ajax.readyState == 4) {
				 if(ajax.status == 200) {
				 	if(id==Sysframe.defaultid){
				 		 set.creatIframe(set,ajax.responseText, title, bStopIndex, id, _href, page,obj,Sysframe,sessionArray,parameter);
				 	}else{

				 	}		
				}else if(ajax.status == 404){
				 	set.PromptBox('地址错误或页面已被删除不存在！', 2);
				 	return false;
				}
			}
		}
		ajax.open('GET', _href, true);
		ajax.send();
	},
	//iframe方法
	iframemethod:function(set,id,title,url,bStopIndex,direction,page,bStop,Sysframe,obj,sessionArray,parameter){
		var topWindow =window.parent.document;
		var show_navLi = set.getElementsByClassName(topWindow,"tabbar-name");
		show_navLi.myMap(function(eve,i){
			 var mid=eve.getAttribute("sys-id");
			if(mid==id){
				bStop = true;
				return false;
			}
		});
		if(!bStop){
			var html=null;
			if(id==Sysframe.defaultid){
				set.creatIframe(set,html, title,bStopIndex,id, url, page,obj,Sysframe,sessionArray,parameter);
			}else{
				set.creatIframe(set,html, title,bStopIndex, id, url, page,obj,Sysframe,sessionArray,parameter);
			}
		}else{
			var html=null;
			if(id==Sysframe.defaultid){
				set.creatIframe(set,html, title,bStopIndex,id, url, page,obj,Sysframe,sessionArray,parameter);
			}else{
				set.creatIframe(set,html, title,bStopIndex, id, url, page,obj,Sysframe,sessionArray,parameter);
			}
		}
	},
	//选项卡切换
	tabshtml:function(set,list,Sysframe){
		var show_nav = set.$(Sysframe.tabs);
		var ul=set.getElementsByClassName(show_nav,"tablist")[0];
		var pagemode=Sysframe.pagemode;
		for(var i=0;i<list.length;i++){
			var id=list[i].id;
			var title=list[i].title;
			var nate=list[i].nate;
			var href=list[i].url;
			var li = document.createElement("li");
			set.addClass(li,'tabbar-name');				
			set.Attributes( li,"sys-id",id);
			set.Attributes( li,"sys-status",nate);
			set.Attributes( li,"title",title);
			set.Attributes( li,"sys-href",href);
			var span = document.createElement("span");
			set.addClass(span,Sysframe.iconfont);
			span.name=href;
			span.title=title;
			set.Attributes( span,"sys-id",id);
			set.Attributes( span,"sys-href",href);
			if(id==Sysframe.defaultid){
				set.Attributes( li,"sys-mode","html");
				set.addClass(li,'home');
				span.innerHTML=Sysframe.iconHome;
				li.appendChild(span);
			}else{
				set.Attributes( li,"sys-mode",pagemode);
				set.addClass(li,'subpages');
				span.innerHTML=title+"<em class='close_icon close_section'></em>";
				set.addClass(span,'tabtitle');
				li.appendChild(span);
			}
			if(nate==1){
				set.addClass(li,'active');
				li.appendChild(span);
			}
			ul.appendChild(li);
		}	
	},
	//获取页面并显示在指定模块中
	creatIframe:function(set,content,title,status,id,href,page,obj,Sysframe,sessionArray,parameter){
		var menu = JSON.parse(window.sessionStorage.getItem(Sysframe.sessionname));
		var home=null,mu=null,column=null,show_nav=null,iframe_box=null,Array=null;
		var iframeArray=[];
		var tabsArray=[];
		id=parseInt(id);
		if(page=="true"){
			
		}else{
		   show_nav = set.$(Sysframe.tabs);
		   iframe_box = set.$(Sysframe.iframe);
		   var show_navLi =set.getElementsByClassName(show_nav,"tabbar-name");
		}
		var pageModule=set.getElementsByClassName(iframe_box,"show_iframe");
		var tabshtml=function(){
			var pagemode=Sysframe.pagemode;
			var ul=set.getElementsByClassName(show_nav,"tablist")[0];
			var li = document.createElement("li");
			set.addClass(li,'tabbar-name');
			set.Attributes( li,"sys-id",id);
			set.Attributes( li,"sys-status",status);
			set.Attributes( li,"title",title);
			set.Attributes( li,"sys-href",href);
			var span = document.createElement("span");
			set.addClass(span,Sysframe.iconfont);
			span.name=href;
			span.title=title;
			set.Attributes( span,"sys-id",id);
			set.Attributes( span,"sys-href",href);
			if(id==Sysframe.defaultid){
				set.Attributes( li,"sys-mode","html");
				set.addClass(li,'home');
				span.innerHTML=Sysframe.iconHome;
				li.appendChild(span);				
			}else{
				set.Attributes( li,"sys-mode",pagemode);
				set.addClass(li,'subpages');
				span.innerHTML=title+"<em class='close_icon close_section'></em>";
				set.addClass(span,'tabtitle');
				li.appendChild(span);
			}
			if(status==1){
				set.addClass(li,'active');
				li.appendChild(span);
			}
			ul.appendChild(li);	
		}
		var iframehtml=function(){
			var div = document.createElement("div");
			set.addClass(div,'show_iframe selected');
			set.Attributes( div,"sys-href",href);
			set.Attributes( div,"sys-id",id);
			div.name=href;
			div.title=title;
			iframe_box.appendChild(div);
			if(content!=null){
				div.innerHTML=content;
			}else{
				var iframe = document.createElement("iframe");
				set.addClass(iframe,'iframe-box');
				iframe.frameBorder="0";
				iframe.src=href;
				set.Attributes(iframe,"sys-href",href);
				div.appendChild(iframe);
				div.style.display="block";
            	set.addClass(div,'selected');
			}
		    if(parameter=="tabs"){
            	show_navLi.myMap(function(eve){
		     	var mid=eve.getAttribute("sys-id");//当前打开id
		     	if(mid!=id){
		     		 set.removeClass(eve,'active');
		     		 set.Attributes( eve,"sys-status",0);
		     	}else{
		     	}
		       });
            }else if(parameter=="refresh"){
            	show_navLi.myMap(function(eve){
            		if(set.hasClass(eve,"active")){
            			div.style.display="block";
            		    set.addClass(div,'selected');
            		}
            	})            	
            }
		};
		if(menu!=null){
			sessionArray=menu;
			if(menu.length!=0){
				Array = menu.some(function(ele, index, array){
				    if(ele.title == title) {
				        return true;
				    }else {
				        return false;
				    }
		       });   
			}else{
				tabshtml();
			}
		}else{
			Array=true;
			sessionArray=sessionArray;
		}
        if(!Array){
	        tabshtml();
	    }
        for(var i=0;i<pageModule.length;i++){
			var show_iframe=pageModule[i];
			var iframename=show_iframe.getAttribute("title");
			var iframeid=show_iframe.getAttribute("sys-id");
			if(parameter=="refresh"){
				var genre;
				show_navLi.myMap(function(eve){
					var mid=eve.getAttribute("sys-id");//当前打开id
					if(iframeid==mid){
						genre=set.hasClass(eve,'active');
					}
				});
				if(genre){
					show_iframe.style.display = "block";
					set.addClass(show_iframe,'selected');
				}else{
					show_iframe.style.display = "none";
					set.removeClass(show_iframe,'selected');
				}
			}else{
				if(title==iframename){
					show_iframe.style.display = "block";
					set.addClass(show_iframe,'selected');
				}else{
					show_iframe.style.display = "none";
					set.removeClass(show_iframe,'selected');

				}
			}
			var newgroup={
				name:iframename,
				id:iframeid
			}
		   iframeArray.push(newgroup); //从新整合数组 
		};
		var iArray = iframeArray.some(function(ele, index, array){
		    if(ele.name == title) {
		        return true;
		    }else {
		        return false;
		    }
		});
		if(!iArray){
           iframehtml();
        }
        if(parameter=="menu" || parameter=="tabs"){
        	show_navLi.myMap(function(eve){
		     	var mid=eve.getAttribute("sys-id");//当前打开id
		     	if(mid!=id){
		     		 set.removeClass(eve,'active');
		     		 set.Attributes( eve,"sys-status",0);
		     	}else{
		     		set.addClass(eve,'active');
		     		set.Attributes( eve,"sys-status",1);
		     	}
		    });
        }
        set.sessionArr(content, title, status, id, href,sessionArray,Sysframe);
        set.menusess(set,Sysframe,obj,parameter);
        if(id==Sysframe.defaultid){	
        	var data={
				content: content,
				title: title,
				nate: status,
				id: id,
				url: href
        	};
        	set.Storage("set",Sysframe.localStorage,data);
        }
	},
	//界面方法调用
	htmltemplate:function(set,json,nameedit,template,structure,level){
		var pid = 0, total=null; //默认值
		var number = json.length;//数量
		if(number != null && number!=0) {
			var s = navigator.userAgent.toLowerCase();
			var BrowserInfo = {
				IsIE: /*@cc_on!@*/ false,
				IsIE9Under: /*@cc_on!@*/ false && (parseInt(s.match(/msie (\d+)/)[1], 10) <= 9),
			};
			if(BrowserInfo.IsIE9Under) {	
				
			} else {
				
			}
			if(template=='html'){
				if(structure=='menu'){
				    set.treedata(json, pid, nameedit);
		            var datalist = set.treedata(json, pid, nameedit);
					set.contenthtml(datalist,nameedit,level);
					var column=set.getElementsByClassName(nameedit,"sys-column");
					for(var i=0;i<column.length;i++){
						column[i].removeAttribute('sys-keyset');
						column[i].removeAttribute('sys-title-value');
						column[i].removeAttribute('sys-url-value');
						column[i].removeAttribute('sys-window-value');
						column[i].removeAttribute('sys-size-value');
						column[i].removeAttribute('sys-direction-value');
					}	
				}else if(structure=='tree'){
					
				}else if(structure=='cycle'){
					set.contenthtml(json,nameedit,level);
					var cycle=set.getElementsByClassName(nameedit,"sys-html");
					for(var i=0;i<cycle.length;i++){
						cycle[i].removeAttribute('sys-keyset');
						cycle[i].removeAttribute('sys-hex');
						cycle[i].removeAttribute('sys-hex-value');
						cycle[i].removeAttribute('sys-hex-background');
						cycle[i].removeAttribute('sys-title-value');
						cycle[i].removeAttribute('sys-url-value');
					}
					
				}	
			}else if(template=='div'){
				if(structure=='menu'){
					
				}else if(structure=='tree'){
					
				}else{
					
				}
			}else{
				
			}
		}
	},
	//方法
	contenthtml:function(oldArr,nameedit,level){
		var set=this;
		var size=oldArr.length;
		var mouIconClose=set.par.mouIconClose; //箭头
		var mouIconOpen=set.par.mouIconOpen;// 箭头
		var contenthtml=function(list,item){
			for(var l=0;l<list.length;l++){
				var m=list[l].parentNode.parentNode;
				if(set.hasClass(m,'sys-childmenu')){
				   set.removeClass(list[l].parentNode,'sys-level');
				}
			  	if(set.hasClass(list[l],'arrow')){
	    	 		set.removeElement(list[l],'arrow');
	    	 	}
				var keyset=list[l].getAttribute("sys-keyset");
				var type=list[l].getAttribute("sys-type");
				var level=list[l].getAttribute("sys-level-value");
				var Child=list[l].getAttribute("sys-child-value");
				var title=list[l].getAttribute("sys-title-value");
				var url=list[l].getAttribute("sys-url-value");
				var window=list[l].getAttribute('sys-window-value');
				var size=list[l].getAttribute('sys-size-value');
				var direction=list[l].getAttribute('sys-direction-value');
				var hex=list[l].getAttribute('sys-hex');
				var hexname=list[l].getAttribute('sys-hex-value');
				var background=list[l].getAttribute('sys-hex-background');
				if(keyset!=null){
					var	name = keyset.split(",");
					for(var k=0;k<name.length;k++){
						var key=name[k];
						var value = item[key];
						if(type=="text"){
							list[l].innerHTML=value;
						}else if(type=="label"){
							var s = document.createAttribute("sys-"+name[k]);
							s.nodeValue =value;
							list[l].setAttributeNode(s);
						}else if(type=="icon"){
							list[l].innerHTML=value;
						}else if(type=="img"){
							var img = document.createElement("img");
							set.addClass(img,'icon-img');
							img.src=value;
							list[l].appendChild(img);
						}else{
							var s = document.createAttribute("sys-"+name[k]);
							s.nodeValue =value;
							list[l].setAttributeNode(s);
						}
						if(name[k]==title){
							var t = document.createAttribute('title');
							t.nodeValue =value;
							list[l].setAttributeNode(t);
							list[l].removeAttribute('sys-'+name[k]+"-value");
						}else if(name[k]==url){
							var u = document.createAttribute('sys-href');
							u.nodeValue =value;
							list[l].setAttributeNode(u);
							list[l].removeAttribute('sys-'+name[k]+"-value");
						}else if(name[k]==window){
							var w = document.createAttribute('sys-window');
							w.nodeValue =value;
							list[l].setAttributeNode(w);
							list[l].removeAttribute('sys-'+name[k]+"-value");
						}else if(name[k]==size){
							var s = document.createAttribute('sys-windowsize');
							s.nodeValue =value;
							list[l].setAttributeNode(s);
							list[l].removeAttribute('sys-'+name[k]+"-value");
						}else if(name[k]==direction){
							var d= document.createAttribute('sys-direction');
							d.nodeValue =value;
							list[l].setAttributeNode(d);
							list[l].removeAttribute('sys-'+name[k]+"-value");
						}else if(name[k]==level){
							var d= document.createAttribute('sys-level');
							d.nodeValue =value;
							list[l].setAttributeNode(d);
							list[l].removeAttribute('sys-'+name[k]+"-value")
							
						}else if(name[k]==hexname){
							var d= document.createAttribute('sys-title');
							d.nodeValue =value;
							list[l].setAttributeNode(d);
							list[l].removeAttribute('sys-'+name[k]+"-value");
						}else if(name[k]==background){
							list[l].style.backgroundColor=value;
							list[l].removeAttribute('sys-'+name[k]+"-value");
						}
					}
					list[l].removeAttribute('sys-keyset');
			    }
			}
		};
		oldArr.myMap(function(item,i){
			var rows = nameedit.children[i];
			var clonedNode = rows.cloneNode(true); // 克隆节点
			nameedit.appendChild(clonedNode); //添加克隆的节点
			if(item.pid && item.pid.length > 0){
			    var list= set.getElementsByClassName(rows,'sys-region');
			    var arrow = document.createElement("i");
			    set.addClass(arrow,'arrow iconfont sys-region' );
			    list[0].appendChild(arrow);
			    arrow.innerHTML=mouIconOpen;
			    var ischek = document.createAttribute("ischek");
			    ischek.nodeValue =false;
			    arrow.setAttributeNode(ischek);
			    var level = document.createAttribute("sys-level");
			    level.nodeValue ="Parent";
			    rows.setAttributeNode(level);
			    set.addClass(rows,'sys-level');
				contenthtml(list,item);
				var lay = document.createElement("ul");
		    	set.addClass(lay,'sys-childmenu clearfix' );
		    	lay.style.display = "none";
		    	lay.style.overflow = "hidden";
		    	var child = nameedit.children[i+1];
		    	var clonedNode = child.cloneNode(true); // 克隆节点
		    	lay.appendChild(clonedNode); //添加克隆的节点
			    rows.appendChild(lay);
				set.contenthtml(item.pid,lay);
		    }else{
		    	i++;
		    	var list= set.getElementsByClassName(rows,'sys-region');
	    	 	contenthtml(list,item);
	    	 	if(i==size){
	    	 		set.removeElement(nameedit.children[size]);
	    	 	}
		    }
       })
	},
	//树状图数据结构重组方法
	treedata: function(oldArr, pid, nameedit) {
		var _this = this;
		var newArr = [];
		var size = oldArr.length;
		oldArr.myMap(function(item) {
			if(item.pid == pid) {
				var newgroup = {};
				for(var i = 0; i < size; i++) {
					var rows = nameedit.children[0];
					var keyvalue = rows.getAttribute("sys-keyset");
					var	result = keyvalue.split(",");
					for(var n = 0; n < result.length; n++) {
						var key=result[n];
						var value = item[key];
						newgroup[key] = '';
					    newgroup[key] += value;
					}	
				}
				var child = _this.treedata(oldArr, item.id, nameedit);
				if(child.length > 0) {
					newgroup.pid = child;
				}
				newArr.push(newgroup);
			}
		});
		return newArr;
	}	
};
(function(w) {
	/** 
	 * map遍历数组 
	 * @param fn [function] 回调函数； 
	 * @param context [object] 上下文； 
	 */
	Array.prototype.myMap = function(fn, context) {
		context = context || window;
		var ary = [];
		if(Array.prototype.map) {
			ary = this.map(fn, context);
		} else {
			for(var i = 0; i < this.length; i++) {
				ary[i] = fn.apply(context, [this[i], i, this]);
			}
		}
		return ary;
	};
	if(!Array.prototype.some) {
	    Array.prototype.some = function(callback) {
		    // 获取数组长度
		    var len = this.length;
		    if(typeof callback != "function") {
		        throw new TypeError();
		    }
		    // thisArg为callback 函数的执行上下文环境
		    var thisArg = arguments[1];
		    for(var i = 0; i < len; i++) {
		        if(i in this && callback.call(thisArg, this[i], i, this)) {
		            return true;
		        }
		    }
		    return false;
	    }
	}
	/*
	 * 鼠标移入移除方法
	 */
	var dqMouse = function(obj) {
		// 函数体
		return new dqMouse.fn.init(obj);
	}
	dqMouse.fn = dqMouse.prototype = {
		// 扩展原型对象
		obj: null,
		dqMouse: "1.0.0",
		init: function(obj) {
			this.obj = obj;
			return this;
		},
		contains: function(a, b) {
			return a.contains ? a != b && a.contains(b) : !!(a.compareDocumentPosition(b) & 16);
		},
		getRelated: function(e) {
			var related;
			var type = e.type.toLowerCase(); //这里获取事件名字
			if(type == 'mouseover') {
				related = e.relatedTarget || e.fromElement
			} else if(type = 'mouseout') {
				related = e.relatedTarget || e.toElement
			}
			return related;
		},
		over: function(fn) {
			var obj = this.obj;
			var _self = this;
			obj.onmouseover = function(e) {
				var related = _self.getRelated(e);
				if(this != related && !_self.contains(this, related)) {
					fn(_self);
				}
			}
			return _self;
		},
		out: function(fn) {
			var obj = this.obj;
			var _self = this;
			obj.onmouseout = function(e) {
				var related = _self.getRelated(e);
				if(obj != related && !_self.contains(obj, related)) {
					fn(_self);
				}
			}
			return _self;
		}
	}
	dqMouse.fn.init.prototype = dqMouse.fn;
	window.dqMouse = window.$$ = dqMouse;
})(window);

