<h2><spring:message code='recent_pinterest_activities_t'/>&nbsp;<a href="${model.pinterestUrl}" target="_blank"><img src="/${model.portalName}/themes/common/images/logos/Pinterest_Logo.png" width="70" height="18" alt="Follow Me on Pinterest" /></a></h2>
title: ${model.pinterestItem.title}
link: ${model.pinterestItem.link}<br/>
description: ${model.pinterestItem.description}<br/>
author: ${model.pinterestItem.author}<br/>
pubdate: ${model.pinterestItem.pubDate}<br/>
guid: ${model.pinterestItem.guid}<br/>
<br/>
<a href="${model.pinterestItem.link}" target="_blank"><img src="${model.pinterestItem[imageUrl]}" alt="${model.pinterestItem.title}"/></a>
<p>${model.pinterestItem[copy]}</p>
