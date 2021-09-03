package net.sintaxis.skywars.core.service;

import me.yushust.inject.InjectAll;

import net.sintaxis.skywars.api.model.Service;
import net.sintaxis.skywars.core.placeholder.applier.PlaceholderApplier;
import net.sintaxis.skywars.core.placeholder.types.MatchPlaceholder;
import net.sintaxis.skywars.core.placeholder.types.UserPlaceholder;

@InjectAll
public class PlaceholderService
        implements Service {

 private PlaceholderApplier placeholderApplier;
 private MatchPlaceholder matchPlaceHolder;
 private UserPlaceholder userPlaceHolder;

 @Override
 public void start() {
  placeholderApplier.getPlaceHolders().add(matchPlaceHolder);
  placeholderApplier.getPlaceHolders().add(userPlaceHolder);
 }
}