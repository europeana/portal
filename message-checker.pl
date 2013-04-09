#!/bin/perl

sub trim($) {
  $str = shift;
	$str =~ s/^\s+//;
	$str =~ s/\s+$//;
	return $str;
}

open EN, "src/main/resources/message_keys/messages_en.properties" or die $!;
%keys = ();
while (<EN>) {
	next if /^\s*#/ || !/=/;
	my($key,$value) = split /\s*=\s*/, $_;
	$key = trim($key);
	$keys{$key} = 1;
}
close EN;

open MSG, "src/main/resources/message_keys/messages.properties" or die $!;
while (<MSG>) {
  next if /^\s*#/ || !/=/;
  my($key,$value) = split /\s*=\s*/, $_;
  $key = trim($key);
  if (!exists $keys{$key}) {
    $line = $_;
    $line =~ s/\s*=\s*/ = /;
    print $line;
  }
}
close MSG;


