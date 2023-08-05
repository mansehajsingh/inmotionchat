package com.inmotionchat.core.data.dto;

import java.util.List;

public record InboxGroupDTO(String name, List<DowntimeDTO> downtimeWindows) {}
